



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/V1/snacks")
public class SnackControllerV1 {

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

    @PostMapping(value = {""})
    public ResponseEntity<?> addFoodAndDrinks(@RequestBody Snack snack) {
        Snack newSnack = snacksService.addNewSnack(snack);
        return new ResponseEntity<>(newSnack, HttpStatus.CREATED);
    }

    @PutMapping(value = {"/{id}"})
    public ResponseEntity<?> updateFoodAndDrinks(@RequestBody Snack Snack, @PathVariable("id") int id) {
        Snack snack = snacksService.updateById(Snack, id);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/snacks/" + snack.getId()));
        return new ResponseEntity<>(snack, HttpStatus.OK);
    }


    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<?> deleteFoodAndDrinks(@PathVariable("id") int id) {
        Snack snack = snacksService.removeSnack(id);
        if (snack != null)
            return new ResponseEntity<>(snack, HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
