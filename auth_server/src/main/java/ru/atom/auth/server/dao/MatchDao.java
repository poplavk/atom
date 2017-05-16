package ru.atom.auth.server.dao;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import ru.atom.auth.server.base.Match;

import javax.persistence.criteria.CriteriaBuilder;

public class MatchDao {
    private static final Logger log = LogManager.getLogger(MatchDao.class);

    private static MatchDao instance = new MatchDao();

    public static MatchDao getInstance() {
        return instance;
    }

    private MatchDao() {
    }

    public void insert(Session session, Match match) {
        session.saveOrUpdate(match);
        log.info("New Match : {}", match);
    }

    public Match getMatchById(Session session, String id) {
        Match match = (Match) session
                .createQuery("from Match where id = :value")
                .setParameter("value", Integer.valueOf(id))
                .uniqueResult();
        return match;
    }

    public Match getMatchById(Session session, Integer id) {
        Match match = (Match) session
                .createQuery("from Match where id = :value")
                .setParameter("value", id)
                .uniqueResult();
        return match;
    }


}
