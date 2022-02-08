package hellojpa;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
public class Album extends Item{

    private String artist;
}