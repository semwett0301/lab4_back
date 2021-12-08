package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="points")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    private double x;
    private double y;
    private double r;
    private boolean hit;
    private String username;

    public Point(double x, double y, double r, boolean hit, String username) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
        this.username = username;
    }
}
