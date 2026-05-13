package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TimetableTest {



    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size(), "В понедельник должна быть одна тренировка");
        assertEquals(singleTrainingSession, mondaySessions.get(0), "Должна вернуться добавленная тренировка");

        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty(), "Во вторник не должно быть тренировок");
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));
        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size(), "В понедельник должна быть одна тренировка");
        assertEquals(mondayChildTrainingSession, mondaySessions.get(0));

        List<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(2, thursdaySessions.size(), "В четверг должно быть две тренировки");
        assertEquals(new TimeOfDay(13, 0), thursdaySessions.get(0).getTimeOfDay(),
                "Первая тренировка должна быть в 13:00");
        assertEquals(new TimeOfDay(20, 0), thursdaySessions.get(1).getTimeOfDay(),
                "Вторая тренировка должна быть в 20:00");

        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty(), "Во вторник не должно быть тренировок");
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> sessionsAt13 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        assertEquals(1, sessionsAt13.size(), "В понедельник в 13:00 должна быть одна тренировка");
        assertEquals(singleTrainingSession, sessionsAt13.get(0));

        List<TrainingSession> sessionsAt14 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        assertTrue(sessionsAt14.isEmpty(), "В понедельник в 14:00 не должно быть тренировок");
    }


    @Test
    void testMultipleSessionsAtSameTime() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Петр", "Сергеевич");
        Coach coach2 = new Coach("Петров", "Иван", "Алексеевич");
        Group group1 = new Group("Акробатика", Age.CHILD, 60);
        Group group2 = new Group("Гимнастика", Age.CHILD, 60);

        TrainingSession session1 = new TrainingSession(group1, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        TrainingSession session2 = new TrainingSession(group2, coach2,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));

        assertEquals(2, sessions.size(), "В одно время могут быть две тренировки");
        assertTrue(sessions.contains(session1), "Должна содержать первую тренировку");
        assertTrue(sessions.contains(session2), "Должна содержать вторую тренировку");
    }

    @Test
    void testGetTrainingSessionsForDayAndTimeEmptyTimetable() {
        Timetable timetable = new Timetable();

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.WEDNESDAY, new TimeOfDay(15, 30));

        assertTrue(sessions.isEmpty(), "Пустое расписание должно вернуть пустой список");
    }

    @Test
    void testGetTrainingSessionsForDayAndTimeDifferentDaysSameTime() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Сидоров", "Алексей", "Петрович");
        Group group = new Group("Балет", Age.ADULT, 90);

        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(18, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(18, 0)));

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(18, 0));
        assertEquals(1, mondaySessions.size());
        assertEquals(DayOfWeek.MONDAY, mondaySessions.get(0).getDayOfWeek());

        List<TrainingSession> fridaySessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.FRIDAY, new TimeOfDay(18, 0));
        assertEquals(1, fridaySessions.size());
        assertEquals(DayOfWeek.FRIDAY, fridaySessions.get(0).getDayOfWeek());
    }


    @Test
    void testGetCountByCoachesBasic() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Смирнов", "Дмитрий", "Александрович");
        Coach coach2 = new Coach("Козлов", "Михаил", "Иванович");
        Group group = new Group("Акробатика", Age.ADULT, 90);

        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.FRIDAY, new TimeOfDay(10, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.TUESDAY, new TimeOfDay(14, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.THURSDAY, new TimeOfDay(14, 0)));

        List<CounterOfTrainings> counts = timetable.getCountByCoaches();

        assertEquals(2, counts.size(), "Должно быть два тренера");

        assertEquals(3, counts.get(0).getCount(), "Первый тренер должен иметь 3 тренировки");
        assertEquals(2, counts.get(1).getCount(), "Второй тренер должен иметь 2 тренировки");

        assertEquals(coach1, counts.get(0).getCoach());
        assertEquals(coach2, counts.get(1).getCoach());
    }

    @Test
    void testGetCountByCoachesEmptyTimetable() {
        Timetable timetable = new Timetable();

        List<CounterOfTrainings> counts = timetable.getCountByCoaches();

        assertTrue(counts.isEmpty(), "Пустое расписание должно вернуть пустой список тренеров");
    }

    @Test
    void testGetCountByCoachesWithEqualCounts() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Новиков", "Сергей", "Петрович");
        Coach coach2 = new Coach("Волков", "Андрей", "Михайлович");
        Group group = new Group("Гимнастика", Age.CHILD, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.TUESDAY, new TimeOfDay(14, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.THURSDAY, new TimeOfDay(14, 0)));

        List<CounterOfTrainings> counts = timetable.getCountByCoaches();

        assertEquals(2, counts.size(), "Должно быть два тренера");
        assertEquals(2, counts.get(0).getCount());
        assertEquals(2, counts.get(1).getCount());

        assertTrue(counts.stream().anyMatch(c -> c.getCoach().equals(coach1)),
                "Должен быть тренер Новиков");
        assertTrue(counts.stream().anyMatch(c -> c.getCoach().equals(coach2)),
                "Должен быть тренер Волков");
    }


    @Test
    void testSortingByTimeInGetTrainingSessionsForDay() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Тестов", "Тест", "Тестович");
        Group group = new Group("Тестовая группа", Age.ADULT, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(15, 30)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(12, 15)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(18, 45)));

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        assertEquals(4, sessions.size());
        assertEquals(new TimeOfDay(9, 0), sessions.get(0).getTimeOfDay());
        assertEquals(new TimeOfDay(12, 15), sessions.get(1).getTimeOfDay());
        assertEquals(new TimeOfDay(15, 30), sessions.get(2).getTimeOfDay());
        assertEquals(new TimeOfDay(18, 45), sessions.get(3).getTimeOfDay());
    }

    @Test
    void testAddAndRetrieveComplexScenario() {
        Timetable timetable = new Timetable();

        Coach coachA = new Coach("А", "А", "А");
        Coach coachB = new Coach("Б", "Б", "Б");
        Group childGroup = new Group("Дети", Age.CHILD, 45);
        Group adultGroup = new Group("Взрослые", Age.ADULT, 60);

        timetable.addNewTrainingSession(new TrainingSession(childGroup, coachA,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(adultGroup, coachB,
                DayOfWeek.MONDAY, new TimeOfDay(14, 0)));
        timetable.addNewTrainingSession(new TrainingSession(childGroup, coachA,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0)));

        timetable.addNewTrainingSession(new TrainingSession(adultGroup, coachB,
                DayOfWeek.TUESDAY, new TimeOfDay(18, 0)));

        List<TrainingSession> monday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(3, monday.size(), "В понедельник должно быть 3 тренировки");

        List<TrainingSession> at10 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        assertEquals(2, at10.size(), "В понедельник в 10:00 должно быть 2 тренировки");


        List<CounterOfTrainings> counts = timetable.getCountByCoaches();
        assertEquals(2, counts.size());
        assertTrue(counts.stream().allMatch(c -> c.getCount() == 2));
    }
}
