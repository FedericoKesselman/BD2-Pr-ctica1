package unlp.info.bd2.model;

import jakarta.persistence.*;

public class Review {

    private Long id;

    private int rating;

    private String comment;

    @OneToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", unique = true, nullable = true)
    private Purchase purchase;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }
}
