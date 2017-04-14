package ru.atom.dbhackaton.server.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import ru.atom.dbhackaton.server.base.Token;
import ru.atom.dbhackaton.server.base.User;

public class TokenDao {
    private static final Logger log = LogManager.getLogger(TokenDao.class);

    private static TokenDao instance = new TokenDao();

    public static TokenDao getInstance() {
        return instance;
    }

    private TokenDao() {
    }

    public void insert(Session session, Token token) {
        session.saveOrUpdate(token);
    }

    public void delete(Session session, Token token) {
        session.delete(token);
    }

    public Token getTokenByUser(Session session, User user) {
        Integer userId = user.getId();
        Token token = (Token) session.createQuery("from Token where user_id = :userId")
                .setParameter("userId", userId)
                .uniqueResult();

        System.out.println("");
        return token;
    }

    public boolean checkUniqueToken(Session session, String valueToken) {
        Token token = (Token) session.createQuery("from Token where token = :value")
                .setParameter("value", valueToken)
                .uniqueResult();
        return token == null;
    }

}
