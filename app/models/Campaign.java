package models;

import javax.persistence.*;

@Entity
@Table(name = "CAMPAIGN")
public class Campaign {
    private long campaignid;
    private String name;
//    private Set<Keyword> keywords = new HashSet<>(0);
    private float bid;
    private float fund;
    private boolean status;
    private String town;
    private int radius;

    public Campaign() {
    }

    public Campaign(String name) {
        this.name = name;
    }

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getCampaignid() {
        return campaignid;
    }

    public void setCampaignid(long campaignid) {
        this.campaignid = campaignid;
    }

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


//    @ManyToMany(targetEntity = Keyword.class)
//    public Set<Keyword> getKeywords() {
//        return keywords;
//    }
//
//    public void setKeywords(Set<Keyword> keywords) {
//        this.keywords = keywords;
//    }

    @Column
    public float getBid() {
        return bid;
    }

    public void setBid(float bid) {
        this.bid = bid;
    }

    @Column
    public float getFund() {
        return fund;
    }

    public void setFund(float fund) {
        this.fund = fund;
    }

    @Column
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Column
    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    @Column
    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}