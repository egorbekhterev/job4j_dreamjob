package ru.job4j.dreamjob.repository;

import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryCandidateRepository implements CandidateRepository {
    private static final MemoryCandidateRepository INSTANCE = new MemoryCandidateRepository();

    private final Map<Integer, Candidate> candidates = new HashMap<>();

    private int nextId = 1;

    private MemoryCandidateRepository() {
        save(new Candidate(0, "George", "No experience", LocalDateTime.now().minusWeeks(1)));
        save(new Candidate(0, "Bob", "Internship", LocalDateTime.now().minusWeeks(2)));
        save(new Candidate(0, "Philipp", "0,5 year experience", LocalDateTime.now().minusWeeks(3)));
        save(new Candidate(0, "William", "1,5 years experience", LocalDateTime.now().minusWeeks(4)));
        save(new Candidate(0, "Vanessa", "2,5 years experience", LocalDateTime.now().minusDays(3)));
        save(new Candidate(0, "Matthew", "5 years experience", LocalDateTime.now().minusDays(12)));
    }

    public static MemoryCandidateRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId++);
        candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public boolean deleteById(int id) {
        return candidates.remove(id) != null;
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(candidate.getId(), (integer, oldCandidate) -> new Candidate(
                oldCandidate.getId(), candidate.getName(), candidate.getDescription(), candidate.getCreationDate()
        )) != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(candidates.get(id));
    }

    @Override
    public Collection<Candidate> getAll() {
        return candidates.values();
    }
}
