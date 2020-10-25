package as.pdt3.api;

import as.pdt3.model.Car;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/cars")
@RestController
public class CarApi {
    private final List<Car> carList;

    public CarApi() {
        this.carList = new ArrayList<>();
        carList.add(new Car(1L, "Mercedes - Benz", "W204", "White"));
        carList.add(new Car(2L, "BMW", "E60", "White"));
        carList.add(new Car(3L, "Opel", "Astra ", "Blue"));
    }

    @GetMapping
    public ResponseEntity getCars() {
        return new ResponseEntity(carList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getCarsById(@PathVariable long id) {
        Optional<Car> result = carList.stream().filter(car -> car.getId() == id).findFirst();
        return result.map(car -> new ResponseEntity<>(car, HttpStatus.OK)).orElseGet(() ->
                new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/color/{color}")
    public ResponseEntity getCarsByColor(@PathVariable String color) {
        List<Car> collect = carList.stream().
                filter(car -> car.getColor().equalsIgnoreCase(color))
                .collect(Collectors.toList());
        if (collect.isEmpty())
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        return new ResponseEntity(collect, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity addCars(@RequestBody Car car) {
        boolean add = carList.add(car);
        if (add)
            return new ResponseEntity(car, HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping
    public ResponseEntity modifyCars(@RequestBody Car car) {
        Optional<Car> first = carList.stream().filter(c -> c.getId() == car.getId()).findFirst();
        if (first.isPresent()) {
            carList.remove(first.get());
            carList.add(car);
            return new ResponseEntity(car, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("{id}")
    public ResponseEntity modifyParameter(@PathVariable long id, @RequestBody Car car) {
        Optional<Car> first = carList.stream().filter(car1 -> car1.getId() == id).findFirst();
        if (first.isPresent()) {
            if (car.getId() > 0)
                first.get().setId(car.getId());
            if (car.getColor() != null)
                first.get().setColor(car.getColor());
            if (car.getMark() != null)
                first.get().setMark(car.getMark());
            if (car.getModel() != null)
                first.get().setModel(car.getModel());
            return new ResponseEntity(first, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCars(@PathVariable long id){
        Optional<Car> first = carList.stream().filter(car -> car.getId() == id).findFirst();
        if (first.isPresent()){
            carList.remove(first.get());
            return new ResponseEntity(first,HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
