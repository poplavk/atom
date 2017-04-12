package ru.atom.dbhackaton.server.base;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user", schema = "auth")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "token", unique = true, nullable = false, length = 20)
    private String token;

    @OneToOne(cascade = CascadeType.PERSIST, targetEntity = User.class)
    private User user;

    public Token(long token) {
        this.token = Long.toString(token);
    }

    public String getToken() {
        return token;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || (getClass() != obj.getClass() && String.class != obj.getClass())) return false;


        if (getClass() == obj.getClass()) {
            Token token = (Token) obj;
            return this.token.equals(token.token);
        }
        if (String.class == obj.getClass()) {
            String valueToken1 = (String) obj;
            return token.equals(valueToken1);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return token.hashCode();
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
