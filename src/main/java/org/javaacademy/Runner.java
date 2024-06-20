package org.javaacademy;

import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.javaacademy.entity.Comment;
import org.javaacademy.entity.TubeUser;
import org.javaacademy.entity.Video;

import java.util.Properties;


public class Runner {
	public static void main(String[] args) {
		Properties properties = createProperties();
		@Cleanup SessionFactory sessionFactory = createSessionFactory(properties);
		@Cleanup Session session = createSession(sessionFactory);
		addDataToDb(session);
		printLastComment(session);
	}

	private static void printLastComment(Session session) {
		TubeUser tubeUserDbJohn = getTubeUserByNickName(session,"John");
		Video videoDbJohn = getVideoByNameAndTubeUser(session, "Мое первое видео", tubeUserDbJohn);
		String text = getLastTextComment(session, videoDbJohn);
		System.out.println(text);
	}

	private static void addDataToDb(Session session) {
		session.beginTransaction();
		addUsers(session);
		addVideo(session);
		addComment(session);
		session.getTransaction().commit();
		session.clear();
	}

	private static String getLastTextComment(Session session, Video video) {
		String hqlComment = "SELECT text FROM Comment WHERE video = :video ORDER BY id DESC";
		return session.createQuery(hqlComment, String.class)
				.setParameter("video", video)
				.setMaxResults(1)
				.uniqueResult();
	}

	private static Video getVideoByNameAndTubeUser(Session session, String name, TubeUser tubeUser) {
		String hqlVideo = "FROM Video WHERE name = :name AND tubeUserOwner = :tubeUserOwner";
		return session.createQuery(hqlVideo, Video.class)
				.setParameter("name", name)
				.setParameter("tubeUserOwner", tubeUser)
				.uniqueResult();
	}

	private static TubeUser getTubeUserByNickName(Session session, String nickName) {
		String hqlUser = "FROM TubeUser WHERE nickName = :nickName";
		return session.createQuery(hqlUser, TubeUser.class)
				.setParameter("nickName", nickName)
				.uniqueResult();
	}

	private static void addComment(Session session) {
		TubeUser tubeUserDbJohn = getTubeUserByNickName(session, "John");
		TubeUser tubeUserDbRick = getTubeUserByNickName(session, "Rick");
		Video videoDbJohn = getVideoByNameAndTubeUser(session, "Мое первое видео", tubeUserDbJohn);
		Comment firstComment = new Comment("Первый комментарий",
				videoDbJohn, tubeUserDbRick);
		session.persist(firstComment);
	}

	private static void addVideo(Session session) {
		TubeUser tubeUserDbJohn = getTubeUserByNickName(session, "John");
		Video firstVideo = new Video("Мое первое видео",
				"Какое-то описание первого видео", tubeUserDbJohn);
		Video secondVideo = new Video("Мое второе видео",
				"Какое-то описание второго видео", tubeUserDbJohn);
		session.persist(firstVideo);
		session.persist(secondVideo);
	}

	private static void addUsers(Session session) {
		TubeUser tubeUserJohn = new TubeUser("John");
		TubeUser tubeUserRick = new TubeUser("Rick");
		session.persist(tubeUserJohn);
		session.persist(tubeUserRick);
	}

	private static Session createSession(SessionFactory sessionFactory) {
		return sessionFactory.openSession();
	}

	private static SessionFactory createSessionFactory(Properties properties) {
		return new Configuration().addProperties(properties)
				.addAnnotatedClass(TubeUser.class)
				.addAnnotatedClass(Video.class)
				.addAnnotatedClass(Comment.class)
				.buildSessionFactory();
	}

	private static Properties createProperties() {
		Properties properties = new Properties();
		properties.put(Environment.URL, "jdbc:postgresql://psql.sl-grp.ru:5432/youtube");
		properties.put(Environment.USER, "user");
		properties.put(Environment.PASS, "7518855");
		properties.put(Environment.DRIVER, "org.postgresql.Driver");
		properties.put(Environment.HBM2DDL_AUTO, "create");
		properties.put(Environment.SHOW_SQL, true);
		properties.put(Environment.FORMAT_SQL, true);
		return properties;
	}

}
