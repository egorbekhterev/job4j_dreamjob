package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Vacancy;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class MemoryVacancyRepository implements VacancyRepository {

    private int nextId = 1;

    private final Map<Integer, Vacancy> vacancies = new HashMap<>();

    private MemoryVacancyRepository() {
        save(new Vacancy(0, "Intern Java Developer", "No experience", LocalDateTime.now().minusMonths(1)));
        save(new Vacancy(0, "Junior Java Developer", "After Internship", LocalDateTime.now().minusDays(5)));
        save(new Vacancy(0, "Junior+ Java Developer", "0,5 - 1 year experience", LocalDateTime.now().minusHours(8)));
        save(new Vacancy(0, "Middle Java Developer", "1 - 2 years experience", LocalDateTime.now().minusMonths(2)));
        save(new Vacancy(0, "Middle+ Java Developer", "2 - 3 years experience", LocalDateTime.now().minusWeeks(2)));
        save(new Vacancy(0, "Senior Java Developer", "3+ years experience", LocalDateTime.now().minusWeeks(7)));
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        vacancy.setId(nextId++);
        vacancies.put(vacancy.getId(), vacancy);
        return vacancy;
    }

    @Override
    public boolean deleteById(int id) {
        return vacancies.remove(id) != null;
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(vacancy.getId(), (id, oldVacancy) -> new Vacancy(
                oldVacancy.getId(), vacancy.getTitle(), vacancy.getDescription(), vacancy.getCreationDate())) != null;
    }

    @Override
    public Optional<Vacancy> findById(int id) {
        return Optional.ofNullable(vacancies.get(id));
    }

    @Override
    public Collection<Vacancy> findAll() {
        return vacancies.values();
    }

}
