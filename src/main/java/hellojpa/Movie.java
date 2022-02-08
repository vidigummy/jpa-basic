package hellojpa;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
public class Movie extends Item{
    private String director;

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    private String actor;
}