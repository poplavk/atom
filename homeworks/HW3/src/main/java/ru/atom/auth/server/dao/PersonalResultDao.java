package ru.atom.auth.server.dao;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import ru.atom.auth.server.base.PersonalResult;

public class PersonalResultDao {
    private static final Logger log = LogManager.getLogger(PersonalResultDao.class);

    private static PersonalResultDao instance = new PersonalResultDao();

    public static PersonalResultDao getInstance() {
        return instance;
    }

    private PersonalResultDao() {
    }

    public void insert(Session session, PersonalResult result) {
        session.saveOrUpdate(result);
        log.info("New Personal Result : {}", result);
    }


}
