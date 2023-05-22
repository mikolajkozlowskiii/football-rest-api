package com.example.football_api.controllers;

import com.example.football_api.email.services.EmailSenderService;
import com.example.football_api.entities.football.LeagueTableView;
import com.example.football_api.repositories.football.LeagueTableViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {
    private final EmailSenderService emailSender;
    private final LeagueTableViewRepository leagueTableViewRepository;
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }
    @GetMapping("/test5")
    public String test2() {
        return "test5.";
    }

    @GetMapping("league")
    public ResponseEntity<List<LeagueTableView>> getLeagueView(){
        return ResponseEntity.ok(leagueTableViewRepository.findByLeagueId(653L));
    }
    @GetMapping("/sendemail")
    public String sendEmail() {
        emailSender.send("mikolajkozlowskiii@gmail.com", "body of email");
        return "email sent";
    }
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }
}
