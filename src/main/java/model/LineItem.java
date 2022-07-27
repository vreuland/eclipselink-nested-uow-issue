package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "LINE_ITEMS")
public class LineItem {

  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  private int id;

  @JoinColumn(name = "ORDER_ID", updatable = false, nullable = false)
  private Order associatedOrder;

  private String product;

  private int quantity;

  @Version
  @Column(name = "JPA_VERSION", nullable = false)
  private long jpaVersion;

  public LineItem() {
  }

  public LineItem(int id, String product, int quantity) {
    this.id = id;
    this.product = product;
    this.quantity = quantity;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Order getAssociatedOrder() {
    return associatedOrder;
  }

  public void setAssociatedOrder(Order associatedOrder) {
    this.associatedOrder = associatedOrder;
  }

  public String getProduct() {
    return product;
  }

  public void setProduct(String product) {
    this.product = product;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
