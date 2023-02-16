package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class MemoryCandidateRepository implements CandidateRepository {

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private final AtomicInteger nextId = new AtomicInteger(0);

    private MemoryCandidateRepository() {
        save(new Candidate(0, "George", "No experience", LocalDateTime.now().minusWeeks(1)));
        save(new Candidate(0, "Bob", "Internship", LocalDateTime.now().minusWeeks(2)));
        save(new Candidate(0, "Philipp", "0,5 year experience", LocalDateTime.now().minusWeeks(3)));
        save(new Candidate(0, "William", "1,5 years experience", LocalDateTime.now().minusWeeks(4)));
        save(new Candidate(0, "Vanessa", "2,5 years experience", LocalDateTime.now().minusDays(3)));
        save(new Candidate(0, "Matthew", "5 years experience", LocalDateTime.now().minusDays(12)));
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId.incrementAndGet());
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
