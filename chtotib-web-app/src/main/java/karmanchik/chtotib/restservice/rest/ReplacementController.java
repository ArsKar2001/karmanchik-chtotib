package karmanchik.chtotib.restservice.rest;

import com.sun.istack.NotNull;
import karmanchik.chtotib.entityservice.entity.Replacement;
import karmanchik.chtotib.entityservice.exception.ResourceNotFoundException;
import karmanchik.chtotib.entityservice.repositories.JpaReplacementRepository;
import karmanchik.chtotib.restservice.assembler.ReplacementAssembler;
import karmanchik.chtotib.restservice.assembler.model.ReplacementModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class ReplacementController implements Controller<Replacement> {
    private final JpaReplacementRepository replacementRepository;
    private final ReplacementAssembler assembler;


    @Override
    @GetMapping("/replacements/{id}")
    public ResponseEntity<?> get(@PathVariable @NotNull Integer id) {
        ReplacementModel model = replacementRepository.findById(id)
                .map(assembler::toModel)
                .orElseThrow(() -> new ResourceNotFoundException(id, Replacement.class));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @GetMapping("/replacements")
    public ResponseEntity<?> getAllByDay(@RequestParam @NotNull LocalDate date) {
        List<ReplacementModel> models = replacementRepository.findAllByDateOrderByPairNumber(date).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(models);
    }

    @Override
    @GetMapping("/replacements/")
    public ResponseEntity<?> getAll() {
        List<ReplacementModel> models = replacementRepository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(models);
    }

    @Override
    @PostMapping(value = "/replacements")
    public ResponseEntity<?> post(@RequestBody Replacement replacement) {
        ReplacementModel model = assembler.toModel(replacementRepository.save(replacement));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(Map.of(
                        "status", "OK",
                        "objects", model
                ));
    }

    @Override
    @PutMapping(value = "/replacements/{id}")
    public ResponseEntity<?> put(@PathVariable @NotNull Integer id,
                                 @RequestBody Replacement replacement) {
        ReplacementModel model = replacementRepository.findById(id)
                .map(r -> {
                    r.setDate(replacement.getDate());
                    r.setGroup(replacement.getGroup());
                    r.setTeachers(replacement.getTeachers());
                    r.setDiscipline(replacement.getDiscipline());
                    r.setPairNumber(replacement.getPairNumber());
                    r.setAuditorium(replacement.getAuditorium());
                    return replacementRepository.save(r);
                }).map(assembler::toModel)
                .orElseThrow(() -> new ResourceNotFoundException(id, Replacement.class));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(Map.of(
                        "status", "OK",
                        "objects", model
                ));
    }

    @Override
    @DeleteMapping("/replacements/{id}")
    public ResponseEntity<?> delete(@PathVariable @NotNull Integer id) {
        replacementRepository.deleteById(id);
        return ResponseEntity.ok("OK");
    }

    @Override
    @DeleteMapping("/replacements")
    public ResponseEntity<?> deleteAll(@RequestParam List<Integer> values) {
        values.forEach(replacementRepository::deleteById);
        return ResponseEntity.ok("OK");
    }

    @Override
    @DeleteMapping("/replacements/")
    public ResponseEntity<?> deleteAll() {
        replacementRepository.deleteAll();
        return ResponseEntity.ok("OK");
    }
}
