



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/V2/snacks")
public class SnackControllerV2 {

    SnackService snacksService;

    @Autowired
    public void setSnacksService(SnackService SnackService) {
        this.snacksService = SnackService;
    }

    @GetMapping(value = {""})
    public ResponseEntity<?> getAllSnacksV2(
            @RequestParam(name = "sort", required = false, defaultValue = "id") String sortingParam,
            @RequestParam(name = "from", required = false, defaultValue = "1") String fromString,
            @RequestParam(name = "to", required = false, defaultValue = Integer.MAX_VALUE + "") String toString,
            @RequestParam(name = "order", required = false, defaultValue = "asc") String order) {
        List<Snack> snacks = snacksService.findAll();
        int from, to;
        try {
            from = Integer.parseInt(fromString);
        } catch (NumberFormatException e) { // If from is not a valid integer, return from start of list
            from = 1;
        }
        try {
            to = Integer.parseInt(toString);
        } catch (NumberFormatException e) { // If to is not a valid integer, return till end of list
            to = snacks.size();
        }
        if (from > to) {
            return new ResponseEntity<>("From should be LTEQ To", HttpStatus.BAD_REQUEST);
        }
        from--;
        order = (order.equals("asc") || order.equals("des")) ? order : "asc";
        switch (sortingParam) {
            case "title":
                snacks.sort(Comparator.comparing(Snack::getTitle));
                break;
            case "price":
                snacks.sort(Comparator.comparing(Snack::getPrice));
                break;
            default:
                snacks.sort(Comparator.comparing(Snack::getId));
                break;
        }
        if (order.equals("des"))
            Collections.reverse(snacks);
        if (from > snacks.size())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        else if (to > snacks.size())
            return new ResponseEntity<>(snacks.subList(from, snacks.size()), HttpStatus.OK);
        else
            return new ResponseEntity<>(snacks.subList(from, to), HttpStatus.OK);
    }

    @GetMapping(value = {"/{id}"})
    public ResponseEntity<?> getFoodAndDrinksByID(@PathVariable("id") int id) {
        Snack snack = snacksService.findById(id);
        if (snack != null) return new ResponseEntity<>(snack, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = {""})
    public ResponseEntity<?> addFoodAndDrinks(@RequestBody Snack Snack) {
        Snack snack = snacksService.addNewSnack(Snack);
        return new ResponseEntity<>(snack, HttpStatus.CREATED);
    }

    @PutMapping(value = {"/{id}"})
    public ResponseEntity<?> updateFoodAndDrinks(@RequestBody Snack Snack, @PathVariable("id") int id) {
        Snack snack = snacksService.updateById(Snack, id);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/snacks/" + snack.getId()));
        return new ResponseEntity<>(snack, HttpStatus.OK);
    }

    @PatchMapping(value = {"/{id}"})
    public ResponseEntity<?> patchSnacksById(@PathVariable("id") int id, @RequestBody Map<Object, Object> payload) {
        Snack snack = snacksService.findById(id);
        payload.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(Snack.class, (String) k);
            if (field != null) {
                field.setAccessible(true);
                String type = field.getType().getName();
                if (type.toLowerCase().contains("float"))
                    ReflectionUtils.setField(field, snack, Float.parseFloat(v.toString()));
                else
                    ReflectionUtils.setField(field, snack, v);
            }
        });
        Snack edited = snacksService.updateById(snack, id);
        return new ResponseEntity<>(edited, HttpStatus.OK);
    }


    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<?> deleteFoodAndDrinks(@PathVariable("id") int id) {
        Snack snack = snacksService.removeSnack(id);
        if (snack != null)
            return new ResponseEntity<>(snack, HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
