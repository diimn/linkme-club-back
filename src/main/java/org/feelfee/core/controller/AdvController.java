package org.feelfee.core.controller;

import org.feelfee.core.exception.ResourceNotFoundException;
import org.feelfee.core.model.Adv;
import org.feelfee.core.model.AdvContent;
import org.feelfee.core.model.Manager;
import org.feelfee.core.model.service.AdvExtendedWithCounterDto;
import org.feelfee.core.model.service.AdvUpload;
import org.feelfee.core.model.service.AdvUploadResponse;
import org.feelfee.core.model.service.MainPageRandomAdv;
import org.feelfee.core.repository.AdvContentRepository;
import org.feelfee.core.repository.AdvRepository;
import org.feelfee.core.service.AdvService;
import org.feelfee.core.service.ImageService;
import org.feelfee.core.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("api/v1/adv")
@CrossOrigin
public class AdvController {

    private final AdvRepository advRepository;

    private final AdvContentRepository advContentRepository;

    private final AdvService advService;

    private final ManagerService managerService;



    @Autowired
    public AdvController(AdvRepository advRepository, AdvContentRepository advContentRepository, AdvService advService, ManagerService managerService) {
        this.advRepository = advRepository;
        this.advContentRepository = advContentRepository;
        this.advService = advService;
        this.managerService = managerService;
    }


    @GetMapping("/getAll")
    public Page<AdvExtendedWithCounterDto> getAll(Model model,
                                                  @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                                                  @RequestHeader("Authorization") String token
    ) {
        System.out.println(pageable);
        System.out.println("TOKEN: " + token);
        //получаем пользователя, затем роль, и делаем запрос для этой роли
//        Page<Adv> result = advRepository.findAll(pageable);
        Page<AdvExtendedWithCounterDto> result = advService.findAllByRole(pageable, token);
        List<AdvExtendedWithCounterDto> advlist = result.getContent();
        System.out.println(advlist);
        int size = 0;
        if (result instanceof Collection<?>) {
            size = ((Collection<?>) result).size();
        }
        model.addAttribute("page", result);
        return result;
    }

    @GetMapping("/getAll1")
    public Page<Adv> getAll1(Model model,
                                                  @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                                                  @RequestHeader("Authorization") String token
    ) {
        System.out.println(pageable);
        System.out.println("TOKEN: " + token);
        //получаем пользователя, затем роль, и делаем запрос для этой роли
//        Page<Adv> result = advRepository.findAll(pageable);
        Page<Adv> result = advService.findAllByRole1(pageable, token);
        List<Adv> advlist = result.getContent();
        System.out.println(advlist);
        int size = 0;
        if (result instanceof Collection<?>) {
            size = ((Collection<?>) result).size();
        }
        model.addAttribute("page", result);
        return result;
    }


    @GetMapping("/getAllSimple")
    public List<Adv> getAllSimple(@RequestParam(required = false, defaultValue = "1") String managerId
    ) {
        List<Adv> result = advRepository.findAllByManager_Id(managerId);
        int size = 0;
        if (result instanceof Collection<?>) {
            size = ((Collection<?>) result).size();
        }
//        Pagea
        return result;
    }

    @GetMapping("/{id}")
    public Adv getAdvById(@PathVariable String id) {
        Adv result = advRepository.findAdvById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Adv not  found"));
        return result;
    }

    @GetMapping("/get/{url}")
    public Adv getAdvByUrl(@PathVariable String url) {
        return advRepository.findAdvByUrl(url)
                .orElseThrow(() -> new ResourceNotFoundException("Adv not  found"));
    }

    @GetMapping("/getContent/{url}")
    public AdvContent getContentByUrl(@PathVariable String url) {
        return advService.getContentByUrl(url);
    }

    @GetMapping("/getContentByUniqUrl/{url}")
    public AdvContent getContentByUniqUrl(@PathVariable String url) {
        return advService.getContentByUniqUrl(url);
    }

    @GetMapping("/getUrlByUniqUrl/{url}")
    public String getUrlByUniqUrl(@PathVariable String url) {
        return advService.getUrlByUniqUrl(url);
    }

    @PostMapping("/")
    public Adv addAdv(
            @Valid @RequestBody Adv adv) {
        if (!advRepository.existsAdvByUrl(adv.getUrl())) {
            return advRepository.save(adv);
        } else {
            return adv;
        }
    }

    //    @PutMapping("/{id}")
    @RequestMapping(value = "/{id}", method = {RequestMethod.OPTIONS, RequestMethod.PUT})
    public Adv updateAdvById(@PathVariable String id,
                             @RequestBody AdvUpload advUpload,
                             @RequestHeader("Authorization") String token) {

        return advService.updateAdvById(id, advUpload, token);


    }

    @DeleteMapping("/{id}")
    public AdvUploadResponse deleteAdvById(@PathVariable String id) {
        advRepository.deleteById(id);
        return new AdvUploadResponse("");
    }

    @PutMapping("/url/{adv_url}")
    public Adv updateAdv(@PathVariable String adv_url,
                         @Valid @RequestBody Adv adv,
                         @RequestHeader("Authorization") String token) {
        if (!advRepository.existsAdvByUrl(adv_url)) {
            throw new ResourceNotFoundException("Adv not found with url: " + adv_url);
        }

        return advRepository.findAdvByUrl(adv_url)
                .map(adv1 -> {
                    adv1.setAdvContent(adv.getAdvContent());
                    return advRepository.save(adv1);
                }).orElseThrow(() -> new ResourceNotFoundException("Adv not found with url: " + adv_url));
    }


    @DeleteMapping("/url/{adv_url}")
    public ResponseEntity<?> deleteAnswer(@PathVariable String adv_url) {
        if (!advRepository.existsAdvByUrl(adv_url)) {
            throw new ResourceNotFoundException("Adv not found with url: " + adv_url);
        }

        return advRepository.findAdvByUrl(adv_url)
                .map(adv -> {
                    advRepository.delete(adv);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Adv not found with url: " + adv_url));

    }

    @PostMapping(path = "create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Adv> createAdv(
            @RequestBody AdvUpload advUpload,
            @RequestHeader("Authorization") String token
    ) {
        Adv result = advService.createAdv(advUpload, token);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getRandomIds")
    public List<MainPageRandomAdv> getRandomIds() {
        return advService.getIds();
    }


}
