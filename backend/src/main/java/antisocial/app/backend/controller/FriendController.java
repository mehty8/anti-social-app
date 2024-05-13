package antisocial.app.backend.controller;

import antisocial.app.backend.component.CatchException;
import antisocial.app.backend.data.dto.FriendsNamesAndRequestsDto;
import antisocial.app.backend.data.dto.FriendsNamesDto;
import antisocial.app.backend.data.dto.ResponseMessageDto;
import antisocial.app.backend.service.IFriendService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/friend")
public class FriendController {

    private IFriendService friendService;

    private CatchException catchException;


    public FriendController(IFriendService friendService, CatchException catchException) {
        this.friendService = friendService;
        this.catchException = catchException;
    }


    @GetMapping
    public ResponseEntity<?> getFriendsNamesAndRequests(){
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = user.getUsername();

            FriendsNamesAndRequestsDto friendsNamesAndRequests = friendService.getFriendsNamesAndRequests(username);

            return ResponseEntity.ok(friendsNamesAndRequests);
        } catch (Exception exception){

            return catchException.catchException(exception, exception.getMessage(), HttpStatus.BAD_REQUEST, this.getClass());
        }
    }

    @GetMapping("finduser/{usernameToSearch}")
    public ResponseEntity<?> getUser(@PathVariable String usernameToSearch){
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String userUsername = user.getUsername();
            Set<String> usernamesString = friendService.findUsers(usernameToSearch, userUsername);

            FriendsNamesDto usernames = new FriendsNamesDto(usernamesString);

            return ResponseEntity.ok(usernames);
        } catch (Exception exception){

            return catchException.catchException(exception, exception.getMessage(), HttpStatus.BAD_REQUEST, this.getClass());
        }
    }

    @PatchMapping("friendrequest/{receiver}")
    public ResponseEntity<ResponseMessageDto> sendFriendsRequest(@PathVariable String receiver){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String sender = user.getUsername();

        return friendRequestHandling(receiver, sender, "requested", "Request sent");
    }

    @PatchMapping("acceptrequest/{sender}")
    public ResponseEntity<ResponseMessageDto> acceptFriendRequest(@PathVariable String sender){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String receiver = user.getUsername();

        return friendRequestHandling(receiver,sender, "accepted", "Request accepted");
    }

    @PatchMapping("denyrequest/{sender}")
    public ResponseEntity<ResponseMessageDto> denyFriendRequest(@PathVariable String sender){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String receiver = user.getUsername();

        return friendRequestHandling(receiver, sender, "denied", "Request denied");
    }


    private ResponseEntity<ResponseMessageDto> friendRequestHandling(String receiver, String sender,
                                                                     String type, String message){
        try {
            friendService.handleFriendRequest(receiver, sender, type);

            ResponseMessageDto simpleResponse = new ResponseMessageDto(message);

            return ResponseEntity.ok(simpleResponse);
        } catch (Exception exception){

            return catchException.catchException(exception, exception.getMessage(), HttpStatus.BAD_REQUEST, this.getClass());
        }
    }
}
