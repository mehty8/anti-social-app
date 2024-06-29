package antisocial.app.backend.controller;

import antisocial.app.backend.data.dto.IResponseDto;
import antisocial.app.backend.errorHandling.exception.component.CatchException;
import antisocial.app.backend.data.dto.FriendsNamesDto;
import antisocial.app.backend.data.dto.ResponseMessageDto;
import antisocial.app.backend.service.IFriendService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

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
    public CompletableFuture<ResponseEntity<IResponseDto>> getFriendsNamesAndRequests(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();

        return friendService.getFriendsNamesAndRequests(username).thenApply(friendsNamesAndRequests
                -> ResponseEntity.ok(friendsNamesAndRequests)).exceptionally(exception
                -> catchException.catchException((Exception) exception, exception.getMessage(),
                HttpStatus.BAD_REQUEST, this.getClass()));

    }

    @GetMapping("finduser/{usernameToSearch}")
    public CompletableFuture<ResponseEntity<IResponseDto>> findUser(@PathVariable String usernameToSearch) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        User user = (User) authentication.getPrincipal();
        String userUsername = user.getUsername();


        return friendService.findUsers(usernameToSearch, userUsername)
                .thenApply(usernamesString -> {
                    SecurityContextHolder.setContext(securityContext);
                    IResponseDto usernames = new FriendsNamesDto(usernamesString);
                    return ResponseEntity.ok(usernames);
                })
                .exceptionally(exception -> catchException.catchException((Exception) exception,
                        exception.getMessage(), HttpStatus.BAD_REQUEST, this.getClass()));
    }

    @PatchMapping("friendrequest/{receiver}")
    public CompletableFuture<ResponseEntity<IResponseDto>> sendFriendsRequest(@PathVariable String receiver){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String sender = user.getUsername();

        return friendRequestHandling(receiver, sender, "Send", "Request sent");
    }

    @PatchMapping("acceptrequest/{sender}")
    public CompletableFuture<ResponseEntity<IResponseDto>> acceptFriendRequest(@PathVariable String sender){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String receiver = user.getUsername();

        return friendRequestHandling(receiver,sender, "Accept", "Request accepted");
    }

    @PatchMapping("denyrequest/{sender}")
    public CompletableFuture<ResponseEntity<IResponseDto>> denyFriendRequest(@PathVariable String sender){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String receiver = user.getUsername();

        return friendRequestHandling(receiver, sender, "Deny", "Request denied");
    }


    private CompletableFuture<ResponseEntity<IResponseDto>> friendRequestHandling(String receiver, String sender,
                                                                     String type, String message){

        return friendService.handleFriendRequest(receiver, sender, type).thenApply(voided -> {
            IResponseDto simpleResponse = new ResponseMessageDto(message);
            return ResponseEntity.ok(simpleResponse);
        }).exceptionally(exception
                -> catchException.catchException((Exception) exception, exception.getMessage(),
                HttpStatus.BAD_REQUEST, this.getClass()));

    }
}
