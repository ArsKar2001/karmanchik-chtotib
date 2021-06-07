package karmanchik.chtotib.restservice.rest;

import com.sun.istack.NotNull;
import karmanchik.chtotib.entityservice.entity.Lesson;
import karmanchik.chtotib.entityservice.exception.ResourceNotFoundException;
import karmanchik.chtotib.entityservice.repositories.JpaLessonsRepository;
import karmanchik.chtotib.restservice.assembler.LessonAssembler;
import karmanchik.chtotib.restservice.assembler.model.LessonModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class LessonController implements Controller<Lesson> {
    private final JpaLessonsRepository lessonsRepository;
    private final LessonAssembler assembler;


    @Override
    @GetMapping("/lessons/{id}")
    public ResponseEntity<?> get(@PathVariable @NotNull Integer id) {
        LessonModel model = lessonsRepository.findById(id)
                .map(assembler::toModel)
                .orElseThrow(() -> new ResourceNotFoundException(id, Lesson.class));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @GetMapping("/lessons")
    public ResponseEntity<?> getAllByDay(@RequestParam @NotNull Integer day) {
        List<LessonModel> models = lessonsRepository.findAllByDayOrderByPairNumber(day).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(models);
    }

    @Override
    @GetMapping("/lessons/")
    public ResponseEntity<?> getAll() {
        List<LessonModel> models = lessonsRepository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(models);
    }

    @Override
    @PostMapping(value = "/lesson")
    public ResponseEntity<?> post(@RequestBody Lesson lesson) {
        LessonModel model = assembler.toModel(lessonsRepository.save(lesson));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(Map.of(
                        "status", "OK",
                        "objects", model
                ));
    }

    @PostMapping(value = "/lessons")
    public ResponseEntity<?> postArray(@RequestBody Map<String, List<Lesson>> lessons) {
        List<LessonModel> models = lessonsRepository.saveAll(lessons.get("lessons")).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(Map.of(
                        "status", "OK",
                        "objects", models
                ));
    }

    @Override
    @PutMapping(value = "/lessons/{id}")
    public ResponseEntity<?> put(@PathVariable @NotNull Integer id,
                                 @RequestBody Lesson lesson) {
        LessonModel model = lessonsRepository.findById(id)
                .map(l -> changeLesson(lesson, l)).map(assembler::toModel)
                .orElseThrow(() -> new ResourceNotFoundException(id, Lesson.class));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(Map.of(
                        "status", "OK",
                        "objects", model
                ));
    }

    @PutMapping("/lessons")
    public ResponseEntity<?> putArray(@RequestBody List<Lesson> lessons) {
        List<LessonModel> models = lessons.stream()
                .map(lesson -> lessonsRepository.findById(lesson.getId())
                        .map(l -> changeLesson(lesson, l))
                        .orElseThrow(() -> new ResourceNotFoundException(lesson.getId(), Lesson.class)))
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(Map.of(
                        "status", "OK",
                        "objects", models
                ));
    }

    @Override
    @DeleteMapping("/lesson")
    public ResponseEntity<?> delete(@RequestParam("id") @NotNull Integer id) {
        lessonsRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/lessons")
    public ResponseEntity<?> deleteAll(@RequestParam List<Integer> values) {
        values.forEach(id -> {
            lessonsRepository.deleteById(id);
            log.info("Deleted lesson by id = {}", id);
        });
        return ResponseEntity.ok("OK");
    }

    @Override
    @DeleteMapping("/lessons/")
    public ResponseEntity<?> deleteAll() {
        lessonsRepository.deleteAll();
        return ResponseEntity.ok("OK");
    }

    private Lesson changeLesson(@RequestBody Lesson oldLesson, Lesson newLesson) {
        newLesson.setDay(oldLesson.getDay());
        newLesson.setGroup(oldLesson.getGroup());
        newLesson.setTeachers(oldLesson.getTeachers());
        newLesson.setDiscipline(oldLesson.getDiscipline());
        newLesson.setPairNumber(oldLesson.getPairNumber());
        newLesson.setWeekType(oldLesson.getWeekType());
        newLesson.setAuditorium(oldLesson.getAuditorium());
        return lessonsRepository.save(newLesson);
    }
}
