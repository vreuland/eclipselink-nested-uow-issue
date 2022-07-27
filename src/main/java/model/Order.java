package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "ORDERS")
public class Order {
  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  private int id;

  @Column(name = "STATUS")
  private String status;

  @OneToMany(mappedBy = "associatedOrder", orphanRemoval = true)
  @OrderColumn(name = "LINE_POSITION")
  private List<LineItem> lineItems;

  @Version
  @Column(name = "JPA_VERSION", nullable = false)
  private long jpaVersion;

  public Order() {
  }

  public Order(int id, String status) {
    this.id = id;
    this.status = status;
    this.lineItems = Collections.emptyList();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setLineItems(List<LineItem> lineItems) {
    this.lineItems = List.copyOf(lineItems);
  }

  public List<LineItem> getLineItems() {
    return lineItems;
  }

  public void addLineItem(LineItem lineItem) {
    lineItem.setAssociatedOrder(this);
    List<LineItem> updatedLineItems = new ArrayList<>(this.lineItems);
    updatedLineItems.add(lineItem);
    this.lineItems = updatedLineItems;
  }

}
