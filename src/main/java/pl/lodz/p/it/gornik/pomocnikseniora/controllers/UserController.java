package pl.lodz.p.it.gornik.pomocnikseniora.controllers;

import org.apache.commons.lang3.concurrent.AbstractCircuitBreaker;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import pl.lodz.p.it.gornik.pomocnikseniora.dtos.*;
import pl.lodz.p.it.gornik.pomocnikseniora.dtos.request.EditProfileRequest;
import pl.lodz.p.it.gornik.pomocnikseniora.dtos.request.EditUserSettingsRequest;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.User;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.UserAccessLevel;
import pl.lodz.p.it.gornik.pomocnikseniora.repositories.AccountDetailsRepository;
import pl.lodz.p.it.gornik.pomocnikseniora.repositories.UserRepository;
import pl.lodz.p.it.gornik.pomocnikseniora.security.JwtTokenProvider;
import pl.lodz.p.it.gornik.pomocnikseniora.services.ValidationErrorService;
import pl.lodz.p.it.gornik.pomocnikseniora.services.UserService;
import pl.lodz.p.it.gornik.pomocnikseniora.validator.RegisterValidator;

import javax.validation.Valid;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private ValidationErrorService validationErrorService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountDetailsRepository accountDetailsRepository;

    @Autowired
    private RegisterValidator registerValidator;


    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ModelMapper modelMapper;


    Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);



    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthDto authDto, BindingResult result) {

        return Optional.ofNullable(validationErrorService.mapValidationErrors(result))
                .orElseGet(() -> ResponseEntity.ok(userService.authenticate(authDto)));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpDto signUpDto, BindingResult result) {

        registerValidator.validate(signUpDto, result);
        UserDto user = modelMapper.map(userService.signUp(signUpDto), UserDto.class);

        return Optional.ofNullable(validationErrorService.mapValidationErrors(result))
                .orElseGet(() -> new ResponseEntity<>(user, HttpStatus.CREATED)); //wrocic do mapValidationErrors ResponseEntity<?>
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {

        List<UserDto> users = userService.getAllUsers().stream()
                .map(p -> modelMapper.map(p, UserDto.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/getVolunteers")
    public ResponseEntity<?> getVolunteers() {

        List<FilterVolunteersDto> users = userService.getAllVolunteers().stream()
                .map(p -> modelMapper.map(p, FilterVolunteersDto.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/getSeniors")
    public ResponseEntity<?> getSeniors() {

        List<AllSeniorsDto> users = userService.getAllSeniors().stream()
                .map(p -> modelMapper.map(p, AllSeniorsDto.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/getFilterVolunteers")
    public ResponseEntity<?> getFilterVolunteers(@RequestParam("cooking") Boolean cooking, @RequestParam("cleanup") Boolean cleanup,
                                                 @RequestParam("transport") Boolean transport, @RequestParam("shopping") Boolean shopping,
                                                 @RequestParam("rehabilitation") Boolean rehabilitation, @RequestParam("nursing") Boolean nursing,
                                                 @RequestParam("monday") Boolean monday, @RequestParam("tuesday") Boolean tuesday,
                                                 @RequestParam("wednesday") Boolean wednesday, @RequestParam("thursday") Boolean thursday,
                                                 @RequestParam("friday") Boolean friday, @RequestParam("saturday") Boolean saturday) {


        List<FilterVolunteersDto> users = userService.getFilterVolunteers(cooking, cleanup, transport, shopping, rehabilitation, nursing,
                monday, tuesday, wednesday, thursday, friday, saturday).stream()
                .map(p -> modelMapper.map(p, FilterVolunteersDto.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(users, HttpStatus.OK);
    }




    @GetMapping("/getVolunteer/{id}")
    public ResponseEntity<?> getVolunteer(@PathVariable Long id) {

        FilterVolunteersDto user = modelMapper.map(userService.getUser(id), FilterVolunteersDto.class);
        Long version = userService.getUserVersion(id);

        return ResponseEntity.ok().eTag("\""+version+"\"").body(user);
    }


    @GetMapping("/getSenior/{id}")
    public ResponseEntity<?> getSenior(@PathVariable Long id) {

        SeniorDto user = modelMapper.map(userService.getUser(id), SeniorDto.class);
        Long version = userService.getUserVersion(id);

        return ResponseEntity.ok().eTag("\""+version+"\"").body(user);
    }

    @GetMapping("/getVolunteerReservation/{id}")
    public ResponseEntity<?> getVolunteerReservation(@PathVariable Long id) {

        VolunteerReservationDto user = modelMapper.map(userService.getUser(id), VolunteerReservationDto.class);
        Long version = userService.getUserVersion(id);

        return ResponseEntity.ok().eTag("\""+version+"\"").body(user);
    }

    @GetMapping("activation/{activationCode}")
    public ResponseEntity<?> VerifyUser(@PathVariable String activationCode) {
        System.out.println("ActivationCodeCOntroller: " + activationCode);
        ActivationUserDto user = modelMapper.map(userService.activateUser(activationCode), ActivationUserDto.class);
//        Long version = userService.getUserVersion(id);

//        return ResponseEntity.ok().eTag("\""+version+"\"").body(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {

        UserDto user = modelMapper.map(userService.getUser(id), UserDto.class);
        Long version = userService.getUserVersion(id);

        return ResponseEntity.ok().eTag("\""+version+"\"").body(user);
    }

    @GetMapping("/getUserDetails/{id}")
    public ResponseEntity<?> getUserDetails(@PathVariable Long id) {

        UserDetailsDto userDetailsDto = modelMapper.map(userService.getUser(id), UserDetailsDto.class);
        Long version = userService.getAccountDetailsVersion(id);

        return ResponseEntity.ok().eTag("\""+version+"\"").body(userDetailsDto);
    }

    @GetMapping("/getUserSettings/{id}")
    public ResponseEntity<?> getUserSettings(@PathVariable Long id) {

        UserSettingsDto userSettingsDto = modelMapper.map(userService.getUser(id), UserSettingsDto.class);
        Long version = userService.getUserVersion(id);

        return ResponseEntity.ok().eTag("\""+version+"\"").body(userSettingsDto);

    }

    @PutMapping("/editAccountDetails")
    public ResponseEntity<?> editAccountDetails(WebRequest webRequest, @RequestBody EditProfileRequest editProfileRequest) {
        String ifMatchValue = webRequest.getHeader("If-Match");
        System.out.println("If-Match editAccountDetails: " + ifMatchValue);

        UserDetailsDto editProfileDto = modelMapper.map(userService.editUser(editProfileRequest.getFirstName(), editProfileRequest.getLastName()
                ,editProfileRequest.getPhoneNumber(),
                editProfileRequest.getLogin(), ifMatchValue), UserDetailsDto.class);

        Long version = userService.getAccountDetailsVersion(editProfileRequest.getLogin());
return ResponseEntity.ok().eTag("\""+ version +"\"").body(editProfileDto);
    }


    @PutMapping("/editVolunteerServices")
    public ResponseEntity<?> editVolunteerServices(@RequestBody VolunteerDto volunteerDto) {



        UserDetailsDto editVolunteerServices = modelMapper.map(userService.editVolunteerServices(volunteerDto), UserDetailsDto.class);

        return new ResponseEntity<>(editVolunteerServices, HttpStatus.OK);

    }

    @PutMapping("/editUserSettings")
    public ResponseEntity<?> editUserSettings(WebRequest webRequest, @RequestBody EditUserSettingsRequest editUserSettingsRequest) {
        String ifMatchValue = webRequest.getHeader("If-Match");
        System.out.println("If-Match LockedUser: " + ifMatchValue);

        UserSettingsDto lockUser = modelMapper.map(userService.editUserSettings(editUserSettingsRequest.getLocked()
                , editUserSettingsRequest.getAdmin(), editUserSettingsRequest.getManager(), editUserSettingsRequest.getUser(),
                editUserSettingsRequest.getLogin(), ifMatchValue), UserSettingsDto.class);
        Long version = userService.getUserVersion(editUserSettingsRequest.getLogin());
        return ResponseEntity.ok().eTag("\""+ version +"\"").body(lockUser);
    }



    @PostMapping("/addImage")
    public ResponseEntity<?> addImage(@RequestParam("imageFile") MultipartFile file,
                                     @RequestParam("login") String login) {
        try {
            byte[] lob = file.getBytes();
            userService.addImage(lob, login);
        } catch (IOException e) {
            logger.info("File upload problem");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/registerSenior")
    public ResponseEntity<?> registerSenior(@RequestParam(value = "textFile", required = false) MultipartFile file, @RequestParam("login") String login,
                                            @RequestParam("password") String password, @RequestParam("email") String email,
                                            @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                                            @RequestParam("phoneNumber") String phoneNumber
                                            ) {

        System.out.println("File: "+ file);
        UserDto registeredSenior = modelMapper.map(userService.registerSenior(file, login, password, email, firstName, lastName, phoneNumber), UserDto.class);
        return new ResponseEntity<>(registeredSenior, HttpStatus.OK);

    }

    @PostMapping("/calculateCertificateLevel")
    public ResponseEntity<?> calculateCertificateLevel(@RequestBody CalculateCertificateLevelDto calculateCertificateLevelDto) {

        CalculateCertificateLevelDto registeredSenior = modelMapper.map(userService.calculateCertificateLevel(calculateCertificateLevelDto), CalculateCertificateLevelDto.class);
        return new ResponseEntity<>(registeredSenior, HttpStatus.OK);
    }

    @PostMapping("/registerVolunteer")
    public ResponseEntity<?> registerVolunteer(@RequestBody VolunteerDto registerVolunteerDto) {

        UserDto registeredSenior = modelMapper.map(userService.registerVolunteer(registerVolunteerDto), UserDto.class);
        return new ResponseEntity<>(registeredSenior, HttpStatus.OK);
    }
    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {

        UserDto forgotPassword = modelMapper.map(userService.sendResetPasswordEmail(forgotPasswordDto), UserDto.class);
        return new ResponseEntity<>(forgotPassword, HttpStatus.OK);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {

        UserDto resetPassword = modelMapper.map(userService.resetPassword(resetPasswordDto), UserDto.class);
        return new ResponseEntity<>(resetPassword, HttpStatus.OK);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> resetPassword(@RequestBody ChangePasswordDto changePasswordDto) {

        UserDto changePassword = modelMapper.map(userService.changePassword(changePasswordDto), UserDto.class);
        return new ResponseEntity<>(changePassword, HttpStatus.OK);
    }


    @GetMapping("/getSeniorPoints/{id}")
    public ResponseEntity<?> getSeniorPoints(@PathVariable Long id) {

        SeniorPointsDto seniorPointsDto = modelMapper.map(userService.getSeniorPoints(id), SeniorPointsDto.class);
        Long version = userService.getUserVersion(id); //zmienic

        return ResponseEntity.ok().eTag("\""+version+"\"").body(seniorPointsDto);

    }

    @GetMapping("/seniorReservationList/{id}")
    public ResponseEntity<?> seniorReservationList(@PathVariable Long id) {

        List<AllSeniorReservationsDto> reservations = userService.listSeniorReservations(id).stream()
                .map(p -> modelMapper.map(p, AllSeniorReservationsDto.class))
                .collect(Collectors.toList());


        return new ResponseEntity<>(reservations, HttpStatus.OK);

    }
    @GetMapping("/getVolunteerReservationDate/{id}")
    public ResponseEntity<?> getVolunteerReservationDate(@PathVariable Long id) {

        List<VolunteerReservationDateDto> reservations = userService.getVolunteerReservationDate(id).stream()
                .map(p -> modelMapper.map(p, VolunteerReservationDateDto.class))
                .collect(Collectors.toList());


        return new ResponseEntity<>(reservations, HttpStatus.OK);

    }





    @PostMapping("/reservation")
    public ResponseEntity<?> reservation(@RequestBody ReservationDto reservationDto) {

        ReservationDto reservation = modelMapper.map(userService.reservation(reservationDto), ReservationDto.class);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }





}
