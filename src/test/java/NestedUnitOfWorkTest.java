import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.persistence.queries.ReadObjectQuery;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.LineItem;
import model.Order;

public class NestedUnitOfWorkTest {

  private static final int ORDER_ID = 1;
  private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
  private final EntityManager entityManager = entityManagerFactory.createEntityManager();

  @BeforeEach
  public void persistOneOrderWithOneLineItem() {
    entityManager.getTransaction().begin();

    Order order = new Order(ORDER_ID, "completed");
    LineItem firstLineItem = new LineItem(1, "nail", 100);
    order.addLineItem(firstLineItem);

    entityManager.persist(firstLineItem);
    entityManager.persist(order);
    entityManager.getTransaction().commit();
    System.out.println("After setup");
  }

  @Test
  void deleteInNestedUOW() {
    entityManager.getTransaction().begin();
    UnitOfWork nestedUnitOfWork = entityManager.unwrap(UnitOfWork.class).acquireUnitOfWork();

    Order myOrder = retrieveOrder(nestedUnitOfWork);
    addSecondLineItemThenDeleteEverything(nestedUnitOfWork, myOrder);

    nestedUnitOfWork.commit();
    nestedUnitOfWork.release();
    entityManager.getTransaction().commit();
  }

  private Order retrieveOrder(UnitOfWork nestedUnitOfWork) {
    ReadObjectQuery query = new ReadObjectQuery(Order.class);
    query.setSelectionId(ORDER_ID);
    query.setCacheUsage(ReadObjectQuery.ConformResultsInUnitOfWork);
    return (Order) nestedUnitOfWork.executeQuery(query);
  }

  private void addSecondLineItemThenDeleteEverything(UnitOfWork nestedUnitOfWork, Order order) {
    LineItem secondLineItem = new LineItem(2, "hammer", 1);
    nestedUnitOfWork.registerNewObject(secondLineItem);
    order.addLineItem(secondLineItem);

    // Delete everything
    order.getLineItems().forEach(nestedUnitOfWork::deleteObject);
    nestedUnitOfWork.deleteObject(order);
  }

  @AfterEach
  public void closeEntityManagerAndFactory() {
    entityManager.close();
    entityManagerFactory.close();
  }

}
