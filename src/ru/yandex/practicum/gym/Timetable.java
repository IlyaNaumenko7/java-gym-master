package ru.yandex.practicum.gym;

import java.util.*;
import java.util.stream.Collectors;

public class Timetable {
    private final Map<DayOfWeek, List<TrainingSession>> timetable;

    public Timetable() {
        this.timetable = new HashMap<>();
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();

        timetable.putIfAbsent(day, new ArrayList<>());
        List<TrainingSession> dayList = timetable.get(day);

        dayList.add(trainingSession);

        dayList.sort(Comparator.comparing(TrainingSession::getTimeOfDay));
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        List<TrainingSession> daySessions = timetable.get(dayOfWeek);

        return daySessions != null
                ? Collections.unmodifiableList(daySessions)
                : Collections.emptyList();
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(
            DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {

        List<TrainingSession> daySessions = timetable.get(dayOfWeek);

        if (daySessions == null) {
            return Collections.emptyList();
        }

        return daySessions.stream()
                .filter(session -> session.getTimeOfDay().equals(timeOfDay))
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        Collections::unmodifiableList
                ));
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> coachCounts = new HashMap<>();

        for (List<TrainingSession> sessions : timetable.values()) {
            for (TrainingSession session : sessions) {
                Coach coach = session.getCoach();
                coachCounts.put(coach, coachCounts.getOrDefault(coach, 0) + 1);
            }
        }

        List<CounterOfTrainings> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : coachCounts.entrySet()) {
            result.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        result.sort((c1, c2) -> Integer.compare(c2.getCount(), c1.getCount()));

        return result;
    }
}