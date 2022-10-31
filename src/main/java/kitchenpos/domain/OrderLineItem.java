package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @Column(name = "order_id", insertable = false, updatable = false)
    private Long orderId;
    @Column(name = "menu_id",  nullable = false)
    private Long menuId;
    @Column(name = "quantity")
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long menuId, final long quantity) {
        this(null, null, menuId, quantity);
    }

    public OrderLineItem(final Long seq, final Long orderId, final Long menuId, final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
