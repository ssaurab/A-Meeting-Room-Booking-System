



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/snacks")
public class SnackController {

    SnackService snacksService;

    @Autowired
    public void setSnacksService(SnackService SnackService) {
        this.snacksService = SnackService;
    }

    @GetMapping(value = {""})
    public ResponseEntity<List<Snack>> getAllFoodAndDrinks() {
        return new ResponseEntity<>(snacksService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = {"/{id}"})
    public ResponseEntity<?> getFoodAndDrinksByID(@PathVariable("id") int id) {
        Snack snack = snacksService.findById(id);
        if (snack != null) return new ResponseEntity<>(snack, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
