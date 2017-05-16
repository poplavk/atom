package ru.atom.auth.server.service;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.atom.auth.server.base.PersonalResult;
import ru.atom.auth.server.base.User;
import ru.atom.auth.server.dao.MatchDao;
import ru.atom.auth.server.dao.PersonalResultDao;
import ru.atom.auth.server.mm.GameSession;
import ru.atom.auth.server.mm.ThreadSafeQueue;
import ru.atom.auth.server.storages.Database;
import ru.atom.auth.server.base.Match;
import ru.atom.auth.server.dao.UserDao;
import ru.atom.auth.server.mm.Connection;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class MatchMakerService {
    private static final Logger logger = LogManager.getLogger(MatchMakerService.class);
    private static ConcurrentHashMap<String, Connection> joins = new ConcurrentHashMap<>();

    public long join(String name, String token) {
        Connection connection;
        if (!joins.containsKey(name)) {
            connection = new Connection(token, name);
            joins.put(name, connection);
            ThreadSafeQueue.getInstance().offer(connection);
        } else {
            connection = joins.get(name);
        }
        if (connection.idNull()) {
            return -1;
        }
        return connection.getSessionIdValue();
    }

    public void finish(String jsonString) throws RuntimeException {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String id = parseId(jsonObject);
        HashMap<String, String> resultsMap = parseResult(jsonObject);
        if (resultsMap.size() != GameSession.PLAYERS_IN_GAME) {
            throw new RuntimeException("Недостаточное количество игроков в результате!");
        }

        Transaction txn = null;

        try (Session session = Database.session()) {
            txn = session.beginTransaction();
            Match match = MatchDao.getInstance().getMatchById(session, id);
            if (match == null) {
                logger.warn("Матч не найден id=" + id);
                throw new RuntimeException("Матч не найден id=" + id);
            }
            Map<User, Integer> results = new HashMap<>();
            for (String userName : resultsMap.keySet()) {
                User user = UserDao.getInstance().getByName(session, userName);
                if (user == null) {
                    throw new RuntimeException("Пользователь не найден login=" + userName);
                }
                Integer result = Integer.valueOf(resultsMap.get(userName));
                results.put(user, result);
                if (joins.containsKey(userName)) {
                    joins.remove(userName);
                }
            }
            for (User user : results.keySet()) {
                PersonalResult personalResult = new PersonalResult(match, user, results.get(user));
                PersonalResultDao.getInstance().insert(session, personalResult);
            }
            txn.commit();
        } catch (NumberFormatException ex) {
            logger.error("Неверный формат результата у игрока!");
            if (txn != null && txn.isActive()) {
                txn.rollback();
            }
        } catch (RuntimeException ex) {
            logger.error("Неверный формат данных!");
            if (txn != null && txn.isActive()) {
                txn.rollback();
            }
        }
    }

    public String parseId(JsonObject jsonObject) {
        return String.valueOf(jsonObject.get("id").getAsString());
    }

    public HashMap<String, String> parseResult(JsonObject jsonObject) {
        jsonObject = jsonObject.get("result").getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> results = jsonObject.entrySet();
        HashMap<String, String> map = new HashMap<>();
        for (Map.Entry<String, JsonElement> elementEntry : results) {
            map.put(elementEntry.getKey(), elementEntry.getValue().getAsString());
        }
        return map;
    }

    public Integer saveMatch(Match match) {
        Transaction txn = null;
        try (Session session = Database.session()) {
            txn = session.beginTransaction();
            MatchDao.getInstance().insert(session, match);
            txn.commit();
        } catch (RuntimeException ex) {
            logger.error("Ошибка при сохранении матча!");
            if (txn != null && txn.isActive()) {
                txn.rollback();
            }
        }
        return match.getId();
    }

    public List<PersonalResult> getResults(String name) {
        List<PersonalResult> list = null;
        Transaction txn = null;
        try (Session session = Database.session()) {
            txn = session.beginTransaction();
            list = PersonalResultDao.getInstance().getByUsername(session, name);
            txn.commit();
        } catch (RuntimeException ex) {
            logger.error("Error get user match information!");
            if (txn != null && txn.isActive()) {
                txn.rollback();
            }
        }
        return list;
    }

    public void addResult(@NotNull Integer id, @NotNull String name, @NotNull Integer result ) throws MatchMakerException {
        Transaction txn = null;
        try (Session session = Database.session()) {
            txn = session.beginTransaction();
            Match match = MatchDao.getInstance().getMatchById(session, id);
            User user = UserDao.getInstance().getUser(session, name);

            PersonalResult personalResult = new PersonalResult(match, user, result);
            PersonalResultDao.getInstance().insert(session, personalResult);
            txn.commit();
        } catch (RuntimeException ex) {
            logger.error("Error add user match information!");
            if (txn != null && txn.isActive()) {
                txn.rollback();
            }
            throw new MatchMakerException("Error add match result");
        }
    }
}
