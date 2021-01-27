package pl.lodz.p.it.gornik.pomocnikseniora.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.lodz.p.it.gornik.pomocnikseniora.dtos.*;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.*;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.ReservationService;
import pl.lodz.p.it.gornik.pomocnikseniora.enums.AccessLevelType;
import pl.lodz.p.it.gornik.pomocnikseniora.enums.ServiceType;
import pl.lodz.p.it.gornik.pomocnikseniora.exceptions.*;
import pl.lodz.p.it.gornik.pomocnikseniora.language.Translator;
import pl.lodz.p.it.gornik.pomocnikseniora.repositories.*;
import pl.lodz.p.it.gornik.pomocnikseniora.security.JwtTokenProvider;
import pl.lodz.p.it.gornik.pomocnikseniora.security.SecurityConstants;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UserService {

    private final UserRepository userRepository;
    private final AccessLevelRepository roleRepository;
    private final SeniorDetailsRepository seniorDetailsRepository;
    private final UserAccessLevelRepository userAccessLevelRepository;
    private final ReservationRepository reservationRepository;
    private final VolunteerDetailsRepository volunteerDetailsRepository;
    private final ReservationServiceRepository reservationServiceRepository;
    private final EmailService emailService;
    private final AccountDetailsRepository accountDetailsRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Autowired
    private ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

    @Autowired
    public UserService(UserRepository userRepository,
                       AccessLevelRepository roleRepository,
                       AccountDetailsRepository accountDetailsRepository, SeniorDetailsRepository seniorDetailsRepository,
                       ReservationRepository reservationRepository,
                       VolunteerDetailsRepository volunteerDetailsRepository,
                       ReservationServiceRepository reservationServiceRepository,
                       EmailService emailService,
                       UserAccessLevelRepository userAccessLevelRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.seniorDetailsRepository = seniorDetailsRepository;
        this.userAccessLevelRepository = userAccessLevelRepository;
        this.accountDetailsRepository = accountDetailsRepository;
        this.reservationRepository = reservationRepository;
        this.volunteerDetailsRepository = volunteerDetailsRepository;
        this.reservationServiceRepository = reservationServiceRepository;
        this.emailService = emailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }



    public JWTLoginSuccessPojo authenticate(AuthDto authDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authDto.getLogin(),
                        authDto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = SecurityConstants.TOKEN_PREFIX + tokenProvider.generateToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new JWTLoginSuccessPojo(true, jwt, roles);
    }

    public User signUp(SignUpDto signUpDto) {
        User newUser = new User();
        AccountDetails accountDetails = new AccountDetails();
//        Set<AccessLevel> roles = new HashSet<>();
        try {
            newUser.setPassword(bCryptPasswordEncoder.encode(signUpDto.getPassword()));
            newUser.setLogin(signUpDto.getLogin());
            newUser.setEmail(signUpDto.getEmail());
            newUser.setActivated(false);
            newUser.setLocked(false);
            newUser.setInvalidLoginAttempts(0);
            newUser.setActivationCode(UUID.randomUUID().toString().replace("-", ""));
            newUser.setResetPasswordCode(UUID.randomUUID().toString().replace("-", ""));
            //roles.add(roleRepository.findByName(AccessLevelType.ROLE_SENIOR));
            //roles.add(roleRepository.findByName(AccessLevelType.ROLE_ADMIN));
            //roles.add(roleRepository.findByName(AccessLevelType.ROLE_VOLUNTEER));
            //newUser.setRoles(roles);

            UserAccessLevel userAccessLevel1 = new UserAccessLevel(newUser, roleRepository.findByName(AccessLevelType.ROLE_SENIOR));
            UserAccessLevel userAccessLevel2 = new UserAccessLevel(newUser, roleRepository.findByName(AccessLevelType.ROLE_VOLUNTEER));
            UserAccessLevel userAccessLevel3 = new UserAccessLevel(newUser, roleRepository.findByName(AccessLevelType.ROLE_ADMIN));
            newUser.getRoles().add(userAccessLevel1);
            newUser.getRoles().add(userAccessLevel2);
            newUser.getRoles().add(userAccessLevel3);


            accountDetails.setFirstName(signUpDto.getFirstName());
            accountDetails.setLastName(signUpDto.getLastName());
            accountDetails.setPhoneNumber(signUpDto.getPhoneNumber());

            newUser.setAccountDetails(accountDetails);
            accountDetails.setUser(newUser);
            //emailService.sendEmail(signUpDto.getEmail());
            //emailService.sendAccountActivationEmail(newUser);
            emailService.sendVerificationEmail(newUser);
            return userRepository.save(newUser);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UsernameAlreadyExistsException(Translator.toLocale("label.userName"));
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getAllVolunteers() {
        List<User> filteredList = new ArrayList<>();
        List<UserAccessLevel> userAccessLevels = userAccessLevelRepository.findByAccessLevelIdEquals(2L);
        for (UserAccessLevel ual : userAccessLevels) {
            User user = ual.getUser();
            filteredList.add(user);
        }
        return filteredList;
    }

    public List<User> getAllSeniors() {
        List<User> filteredList = new ArrayList<>();
        List<UserAccessLevel> userAccessLevels = userAccessLevelRepository.findByAccessLevelIdEquals(1L);
        for (UserAccessLevel ual : userAccessLevels) {
            User user = ual.getUser();
            filteredList.add(user);
        }
        return filteredList;
    }

    public List<User> getFilterVolunteers(Boolean cooking, Boolean cleanup, Boolean transport, Boolean shopping, Boolean rehabilitation, Boolean nursing,
                                          Boolean monday, Boolean tuesday, Boolean wednesday, Boolean thursday, Boolean friday, Boolean saturday) {
        List<User> filteredList = new ArrayList<>();




        List<UserAccessLevel> userAccessLevels = userAccessLevelRepository.findByAccessLevelIdEquals(2L);
        for (UserAccessLevel ual : userAccessLevels) {
            User user = ual.getUser();


            if (cooking) {
                if(!user.getVolunteerDetails().getCooking()) {
                    continue;
                }
            }
            if (cleanup) {
                if (!user.getVolunteerDetails().getCleanup()) {
                    continue;
                }
            }
            if (transport) {
                if (!user.getVolunteerDetails().getTransport()) {
                    continue;
                }
            }
            if (shopping) {
                if (!user.getVolunteerDetails().getShopping()) {
                    continue;
                }
            }
            if (rehabilitation) {
                if (!user.getVolunteerDetails().getRehabilitation()) {
                    continue;
                }
            }
            if (nursing) {
                if (!user.getVolunteerDetails().getNursing()) {
                    continue;
                }
            }

            if (monday) {
                if (!user.getVolunteerDetails().getMonday()) {
                    continue;
                }
            }

            if (tuesday) {
                if (!user.getVolunteerDetails().getTuesday()) {
                    continue;
                }
            }

            if (wednesday) {
                if (!user.getVolunteerDetails().getWednesday()) {
                    continue;
                }
            }

            if (thursday) {
                if (!user.getVolunteerDetails().getThursday()) {
                    continue;
                }
            }

            if (friday) {
                if (!user.getVolunteerDetails().getFriday()) {
                    continue;
                }
            }

            if (saturday) {
                if (!user.getVolunteerDetails().getSaturday()) {
                    continue;
                }
            }

            filteredList.add(user);
        }
        return filteredList;
    }


    public User getUser(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("CustomNotFoundException"));
    }


    public User activateUser(String activationCode) {
        System.out.println("ActivationCode: " + activationCode);
        User user = userRepository.findByActivationCode(activationCode)
                .orElseThrow(() -> new ActivationCodeException("ActivationCodeNotFound"));
        if (user.getActivated()) {
            throw new ActivationCodeException("Account is already activated");
        }
        user.setActivated(true);
        userRepository.save(user);
        return user;
    }

    public User sendResetPasswordEmail(ForgotPasswordDto forgotPasswordDto) {
       try {
           String email = forgotPasswordDto.getForgotPassword();
           System.out.println(email);
           User user = userRepository.findByEmail(email)
                   .orElseThrow(() -> new ActivationCodeException("Account with this email not found"));

           emailService.sendPasswordResetEmail(user);
           return user;
       } catch (Exception e) {

           e.printStackTrace();
           throw new ActivationCodeException("Account with this email not found");
       }
    }

    public User resetPassword(ResetPasswordDto resetPasswordDto) {
        try {
            String resetPasswordCode = resetPasswordDto.getResetPasswordCode();
            System.out.println(resetPasswordCode);
            User user = userRepository.findByResetPasswordCode(resetPasswordCode)
                    .orElseThrow(() -> new ActivationCodeException("Account with this email not found"));

            user.setPassword(bCryptPasswordEncoder.encode(resetPasswordDto.getPassword()));
            userRepository.save(user);
            return user;
        } catch (Exception e) {

            e.printStackTrace();
            throw new ActivationCodeException("Account with this email not found");
        }
    }


    public User changePassword(ChangePasswordDto changePasswordDto) {
        try {


            User user = userRepository.findById(changePasswordDto.getId())
                    .orElseThrow(() -> new ActivationCodeException("nie znaleziono"));
            String changePassword = changePasswordDto.getNewPassword();
            String encodedChangePassword = bCryptPasswordEncoder.encode(changePassword);
            user.setPassword(encodedChangePassword);
            userRepository.save(user);
            return user;

        } catch (Exception e) {

            e.printStackTrace();
            throw new ActivationCodeException("Account with this email not found");
        }
    }


    public User editUser(String firstName, String lastName, String phoneNumber, String login, String eTag) {

        User editedAccount = userRepository.findByLogin(login)
                .orElseThrow(() -> new CustomNotFoundException("CustomNotFoundException"));

        if(!eTag.equals("\""+editedAccount.getAccountDetails().getVersion() + "\"")) {
            throw new EtagException("etagException");
        }

        try{
            editedAccount.getAccountDetails().setFirstName(firstName);
            editedAccount.getAccountDetails().setLastName(lastName);
            editedAccount.getAccountDetails().setPhoneNumber(phoneNumber);
             return userRepository.save(editedAccount);
        } catch (OptimisticLockingFailureException ex) {
            throw new CustomOptimisticLockException("customOptimisticLock");
        }

    }


    public User editUserSettings(Boolean locked, Boolean admin, Boolean manager, Boolean user, String login, String eTag) {

        User editedAccount = userRepository.findByLogin(login)
                .orElseThrow(() -> new CustomNotFoundException("CustomNotFoundException"));

        //Set<AccessLevel> roles = new HashSet<>();
//        List<UserAccessLevel> roles = new ArrayList<>();
        UserAccessLevel userAccessLevelSenior = new UserAccessLevel(editedAccount, roleRepository.findByName(AccessLevelType.ROLE_SENIOR));
        UserAccessLevel userAccessLevelVolunteer = new UserAccessLevel(editedAccount, roleRepository.findByName(AccessLevelType.ROLE_VOLUNTEER));
        UserAccessLevel userAccessLevelAdmin = new UserAccessLevel(editedAccount, roleRepository.findByName(AccessLevelType.ROLE_ADMIN));
        if (admin) {


            boolean containsAdmin = editedAccount.getRoles().stream().anyMatch(o-> o.getAccessLevel().getName().equals(AccessLevelType.ROLE_ADMIN));
            System.out.println("Contains admin: " + containsAdmin);
            if (!containsAdmin) {
                editedAccount.getRoles().add(userAccessLevelAdmin);
            }

        } else {
            boolean containsAdmin = editedAccount.getRoles().stream().anyMatch(o-> o.getAccessLevel().getName().equals(AccessLevelType.ROLE_ADMIN));
            System.out.println("Contains admin: " + containsAdmin);
            if (containsAdmin) {
                editedAccount.getRoles().remove(editedAccount.getRoles().stream().filter(o->o.getAccessLevel().getName().equals(AccessLevelType.ROLE_ADMIN))
                        .findAny().orElseThrow(() -> new CustomNotFoundException("problemkontaadmin")));
                System.out.println("GetRolesLiczbaAdmin: " + editedAccount.getRoles().size());
            }
        }



        if(manager) {
            boolean containsManager = editedAccount.getRoles().stream().anyMatch(o-> o.getAccessLevel().getName().equals(AccessLevelType.ROLE_VOLUNTEER));
            System.out.println("Contains manager: " + containsManager);
            if (!containsManager) {
                editedAccount.getRoles().add(userAccessLevelVolunteer);
            }

        } else {
            boolean containsManager = editedAccount.getRoles().stream().anyMatch(o-> o.getAccessLevel().getName().equals(AccessLevelType.ROLE_VOLUNTEER));
            System.out.println("Contains manager: " + containsManager);
            if (containsManager) {
                editedAccount.getRoles().remove(editedAccount.getRoles().stream().filter(o->o.getAccessLevel().getName().equals(AccessLevelType.ROLE_VOLUNTEER))
                        .findAny().orElseThrow(() -> new CustomNotFoundException("problemkontamanager")));
                System.out.println("GetRolesLiczbaManager: " + editedAccount.getRoles().size());
            }
        }



        if(user) {


            boolean containsSenior = editedAccount.getRoles().stream().anyMatch(o-> o.getAccessLevel().getName().equals(AccessLevelType.ROLE_SENIOR));
            System.out.println("Contains user: " + containsSenior);
            if (!containsSenior) {
                editedAccount.getRoles().add(userAccessLevelSenior);
            }
        } else {
            boolean containsSenior = editedAccount.getRoles().stream().anyMatch(o-> o.getAccessLevel().getName().equals(AccessLevelType.ROLE_SENIOR));
            System.out.println("Contains user: " + containsSenior);
            System.out.println("GetRolesLiczbaSeniorPrzed: " + editedAccount.getRoles().size());
            if (containsSenior) {
                editedAccount.getRoles().remove(editedAccount.getRoles().stream().filter(o->o.getAccessLevel().getName().equals(AccessLevelType.ROLE_SENIOR))
                        .findAny().orElseThrow(() -> new CustomNotFoundException("problemkontasenior")));
                System.out.println("GetRolesLiczbaSenior: " + editedAccount.getRoles().size());
            }
        }

        if(!eTag.equals("\""+editedAccount.getVersion() + "\"")) {
            throw new EtagException("etagException");
        }

        try{
            editedAccount.setLocked(locked);
//            editedAccount.setRoles(roles);
            return userRepository.save(editedAccount);
        } catch (OptimisticLockingFailureException ex) {
            throw new CustomOptimisticLockException("customOptimisticLock");
        }

    }


    public void addImage(byte[] lob, String login) {
        Image image = new Image();
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new CustomNotFoundException("ImageNotFound"));

        image.setLob(lob);

        user.setImage(image);
        image.setUser(user);
        userRepository.save(user);
    }


    public Long getAccountDetailsVersion(Long userId) {
        AccountDetails accountDetails = accountDetailsRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomNotFoundException("CustomNotFoundException"));
        return accountDetails.getVersion();

    }

    public Long getAccountDetailsVersion(String login) {
        AccountDetails accountDetails = accountDetailsRepository.findByUserLogin(login)
                .orElseThrow(() -> new CustomNotFoundException("CustomNotFoundException"));
        return accountDetails.getVersion();

    }


    public Long getUserVersion (Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomNotFoundException("CustomNotFoundException"));
        return user.getVersion();

    }

    public Long getUserVersion (String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new CustomNotFoundException("CustomNotFoundException"));
        return user.getVersion();

    }


    public User registerSenior(MultipartFile file, String login,
                               String password,
                               String email,
                               String firstName,
                               String lastName,
                               String phoneNumber ) {
        User newUser = new User();
        AccountDetails accountDetails = new AccountDetails();
        SeniorDetails seniorDetails = new SeniorDetails();

        List<UserAccessLevel> roles = new ArrayList<>();


        if(file != null) {

            try {
                byte[] lob = file.getBytes();
                seniorDetails.setLob(lob);
                seniorDetails.setAcceptCertificate(false);

            } catch (IOException e) {
                System.out.println("File upload problem"); //rzucic wyjatek

            }
        }


        try {
            newUser.setPassword(bCryptPasswordEncoder.encode(password));
            newUser.setLogin(login);
            newUser.setEmail(email);
            newUser.setActivated(false);
            newUser.setLocked(false);
            newUser.setInvalidLoginAttempts(0);
            newUser.setActivationCode(UUID.randomUUID().toString().replace("-", ""));
            newUser.setResetPasswordCode(UUID.randomUUID().toString().replace("-", ""));
//            roles.add(roleRepository.findByName(AccessLevelType.ROLE_SENIOR));
//            newUser.setRoles(roles);
            UserAccessLevel userAccessLevelSenior = new UserAccessLevel(newUser, roleRepository.findByName(AccessLevelType.ROLE_SENIOR));
            roles.add(userAccessLevelSenior);
            newUser.setRoles(roles);


            accountDetails.setFirstName(firstName);
            accountDetails.setLastName(lastName);
            accountDetails.setPhoneNumber(phoneNumber);



            newUser.setAccountDetails(accountDetails);
            accountDetails.setUser(newUser);

//            seniorDetails.setLevelName(registerSeniorDto.getLevelName());
//            if(registerSeniorDto.getLevelName().equals("NONE")) {
//                seniorDetails.setPoints(20);
//            } else if (registerSeniorDto.getLevelName().equals("LIGHT")) {
//                seniorDetails.setPoints(30);
//            } else if (registerSeniorDto.getLevelName().equals("MODERATE")) {
//                seniorDetails.setPoints(40);
//            } else if (registerSeniorDto.getLevelName().equals("SIGNIFICANT")) {
//                seniorDetails.setPoints(50);
//            }

            seniorDetails.setLevelName("NONE");
            seniorDetails.setPoints(20);

            newUser.setSeniorDetails(seniorDetails);
            seniorDetails.setUser(newUser);

            emailService.sendVerificationEmail(newUser);
            return userRepository.save(newUser);
        } catch (Exception e) {

            throw new UsernameAlreadyExistsException(e.getMessage());
        }
    }


    public User registerVolunteer(VolunteerDto registerVolunteerDto) {
        User newUser = new User();
        AccountDetails accountDetails = new AccountDetails();
        VolunteerDetails volunteerDetails = new VolunteerDetails();
//        Set<AccessLevel> roles = new HashSet<>();
        List<UserAccessLevel> roles = new ArrayList<>();
        try {
            newUser.setPassword(bCryptPasswordEncoder.encode(registerVolunteerDto.getPassword()));
            newUser.setLogin(registerVolunteerDto.getLogin());
            newUser.setEmail(registerVolunteerDto.getEmail());
            newUser.setActivated(false);
            newUser.setLocked(false);
            newUser.setInvalidLoginAttempts(0);
            newUser.setActivationCode(UUID.randomUUID().toString().replace("-", ""));
            newUser.setResetPasswordCode(UUID.randomUUID().toString().replace("-", ""));
//            roles.add(roleRepository.findByName(AccessLevelType.ROLE_VOLUNTEER));
//            newUser.setRoles(roles);
            UserAccessLevel userAccessLevelVolunteer = new UserAccessLevel(newUser, roleRepository.findByName(AccessLevelType.ROLE_VOLUNTEER));
            roles.add(userAccessLevelVolunteer);
            newUser.setRoles(roles);



            accountDetails.setFirstName(registerVolunteerDto.getFirstName());
            accountDetails.setLastName(registerVolunteerDto.getLastName());
            accountDetails.setPhoneNumber(registerVolunteerDto.getPhoneNumber());

            newUser.setAccountDetails(accountDetails);
            accountDetails.setUser(newUser);


            volunteerDetails.setCooking(registerVolunteerDto.getCooking());
            volunteerDetails.setCleanup(registerVolunteerDto.getCleanup());
            volunteerDetails.setTransport(registerVolunteerDto.getTransport());
            volunteerDetails.setShopping(registerVolunteerDto.getShopping());
            volunteerDetails.setRehabilitation(registerVolunteerDto.getRehabilitation());
            volunteerDetails.setNursing(registerVolunteerDto.getNursing());

            volunteerDetails.setMonday(registerVolunteerDto.getMonday());
            volunteerDetails.setTuesday(registerVolunteerDto.getTuesday());
            volunteerDetails.setWednesday(registerVolunteerDto.getWednesday());
            volunteerDetails.setThursday(registerVolunteerDto.getThursday());
            volunteerDetails.setFriday(registerVolunteerDto.getFriday());
            volunteerDetails.setSaturday(registerVolunteerDto.getSaturday());

            newUser.setVolunteerDetails(volunteerDetails);
            volunteerDetails.setUser(newUser);



            emailService.sendVerificationEmail(newUser);
            return userRepository.save(newUser);
        } catch (Exception e) {
//            throw new UsernameAlreadyExistsException(Translator.toLocale("label.userName"));
            throw new UsernameAlreadyExistsException(e.getMessage());
        }
    }

    public SeniorDetails calculateCertificateLevel (CalculateCertificateLevelDto calculateCertificateLevelDto) {
       SeniorDetails seniorDetails = seniorDetailsRepository.findByUserId(calculateCertificateLevelDto.getId())
               .orElseThrow(() -> new CustomNotFoundException("CustomNotFoundException"));
        seniorDetails.setLevelName(calculateCertificateLevelDto.getLevelName());
        seniorDetails.setAcceptCertificate(true);
       return seniorDetailsRepository.save(seniorDetails);
    }

    public VolunteerDetails editVolunteerServices (VolunteerDto volunteerDto) {


        VolunteerDetails volunteerDetails = volunteerDetailsRepository.findByUserId(volunteerDto.getId())
                .orElseThrow(() -> new CustomNotFoundException("CustomNotFoundException"));

        volunteerDetails.setCooking(volunteerDto.getCooking());
        volunteerDetails.setCleanup(volunteerDto.getCleanup());
        volunteerDetails.setTransport(volunteerDto.getTransport());
        volunteerDetails.setShopping(volunteerDto.getShopping());
        volunteerDetails.setRehabilitation(volunteerDto.getRehabilitation());
        volunteerDetails.setNursing(volunteerDto.getNursing());
        volunteerDetails.setMonday(volunteerDto.getMonday());
        volunteerDetails.setTuesday(volunteerDto.getTuesday());
        volunteerDetails.setWednesday(volunteerDto.getWednesday());
        volunteerDetails.setThursday(volunteerDto.getThursday());
        volunteerDetails.setFriday(volunteerDto.getFriday());
        volunteerDetails.setSaturday(volunteerDto.getSaturday());
        return volunteerDetailsRepository.save(volunteerDetails);
    }



    public SeniorDetails getSeniorPoints(Long userId) {  //ZABEZPIECZYC BO MOZE BYC ID WOLONTARIUSZA NP
        return seniorDetailsRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomNotFoundException("CustomNotFoundException"));


    }

    public Reservation reservation(ReservationDto reservationDto) {
        System.out.println("Punkty: " + reservationDto.getTotalPoints() + " IdSeniora "+ reservationDto.getSeniorId() +
                " IdWolontariusza " + reservationDto.getVolunteerId() + " Data " + reservationDto.getReservationDate());


        Reservation reservation = new Reservation();
        User senior = userRepository.findById(reservationDto.getSeniorId()).orElseThrow(() -> new CustomNotFoundException("CustomNotFoundException"));
        User volunteer = userRepository.findById(reservationDto.getVolunteerId()).orElseThrow(() -> new CustomNotFoundException("CustomNotFoundException"));

        System.out.println("Senior points: " + senior.getSeniorDetails().getPoints());

        reservation.setSenior(senior);
        reservation.setVolunteer(volunteer);
        reservation.setReservationDate(reservationDto.getReservationDate());
        if (reservationDto.getTotalPoints() > senior.getSeniorDetails().getPoints()) {
            throw new NotEnoughPointsException(Translator.toLocale("reservation.points"));
        } else {
            reservation.setTotalPoints(reservationDto.getTotalPoints());
            Integer currentSeniorPoints = senior.getSeniorDetails().getPoints();
            Integer reservationPoints = reservationDto.getTotalPoints();
            Integer pointsAfterReservation = currentSeniorPoints - reservationPoints;
            senior.getSeniorDetails().setPoints(pointsAfterReservation);
        }

        if (reservationDto.getCookingService()) {
            ReservationService reservationServiceCooking = new ReservationService(reservation, ServiceType.COOKING);
            reservation.getServices().add(reservationServiceCooking);
        }
        if (reservationDto.getCleanupService()) {
            ReservationService reservationServiceCleanup = new ReservationService(reservation, ServiceType.CLEANUP);
            reservation.getServices().add(reservationServiceCleanup);
        }
        if (reservationDto.getTransportService()) {
            ReservationService reservationServiceTransport = new ReservationService(reservation, ServiceType.TRANSPORT);
            reservation.getServices().add(reservationServiceTransport);
        }
        if (reservationDto.getShoppingService()) {
            ReservationService reservationServiceShopping = new ReservationService(reservation, ServiceType.SHOPPING);
            reservation.getServices().add(reservationServiceShopping);
        }
        if (reservationDto.getRehabilitationService()) {
            ReservationService reservationServiceRehabilitation = new ReservationService(reservation, ServiceType.REHABILITATION);
            reservation.getServices().add(reservationServiceRehabilitation);
        }
        if (reservationDto.getNursingService()) {
            ReservationService reservationServiceNursing = new ReservationService(reservation, ServiceType.NURSING);
            reservation.getServices().add(reservationServiceNursing);
        }




        return reservationRepository.save(reservation);
    }


    public List<Reservation> listSeniorReservations(Long id) {

        return reservationRepository.findBySeniorId(id);


    }


    public List<Reservation> getVolunteerReservationDate(Long id) {

        return reservationRepository.findByVolunteerId(id);


    }



}
