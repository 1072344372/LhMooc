package asia.lhweb.lhmooc.model;

import java.math.BigDecimal;

/**
 * 购物项
 *
 * @author 罗汉
 * @date 2024/03/13
 */
public class CartItem {
    private String pimage;

    public String getPimage() {
        return pimage;
    }

    public void setPimage(String pimage) {
        this.pimage = pimage;
    }
    private Integer id;
    private String name;
    private BigDecimal price;
    private Integer count;//课程数量
    private BigDecimal totalPrice;//总价格

    public CartItem() {
    }

    public CartItem(Integer id, String name, BigDecimal price, Integer count, BigDecimal totalPrice) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.totalPrice = totalPrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "pimage='" + pimage + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
