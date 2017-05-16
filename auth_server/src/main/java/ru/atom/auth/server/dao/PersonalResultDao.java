package ru.atom.auth.server.dao;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import ru.atom.auth.server.base.PersonalResult;
import ru.atom.auth.server.base.User;

import java.util.List;

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

    public List<PersonalResult> getByUsername(Session session, String name) {
        User user = UserDao.getInstance().getByName(session, name);
        return (List<PersonalResult>) session
                .createQuery("from PersonalResult where user_id = :user")
                .setParameter("user", user).list();
    }


}
