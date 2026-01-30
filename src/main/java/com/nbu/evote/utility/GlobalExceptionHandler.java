package com.nbu.evote.utility;

import com.nbu.evote.exception.CitizenNotFoundException;
import com.nbu.evote.exception.DuplicateVoteException;
import com.nbu.evote.exception.PartyNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MultipartException.class)
    public String handleMultipartException(MultipartException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "File upload error: " + e.getCause().getMessage());
        return "redirect:/uploadStatus";
    }

    @ExceptionHandler(DuplicateVoteException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDuplicateVote(DuplicateVoteException e, Model model) {
        model.addAttribute("error", e.getMessage());
        model.addAttribute("errorTitle", "Duplicate Vote Detected");
        return "../static/error";
    }

    @ExceptionHandler(CitizenNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleCitizenNotFound(CitizenNotFoundException e, Model model) {
        model.addAttribute("error", e.getMessage());
        model.addAttribute("errorTitle", "Citizen Not Found");
        return "../static/error";
    }

    @ExceptionHandler(PartyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handlePartyNotFound(PartyNotFoundException e, Model model) {
        model.addAttribute("error", e.getMessage());
        model.addAttribute("errorTitle", "Party Not Found");
        return "../static/error";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgument(IllegalArgumentException e, Model model) {
        model.addAttribute("error", e.getMessage());
        model.addAttribute("errorTitle", "Invalid Request");
        return "../static/error";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception e, Model model) {
        model.addAttribute("error", "An unexpected error occurred. Please try again later.");
        model.addAttribute("errorTitle", "System Error");
        return "../static/error";
    }
}