package learn.barrel_of_books.domain;


import learn.barrel_of_books.data.StoreRepository;
import learn.barrel_of_books.models.Store;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {

    private final StoreRepository repository;

    public StoreService(StoreRepository repository) {
        this.repository = repository;
    }

    public List<Store> findAll() {
        return repository.findAll();
    }

    public Store findById(int id) {
        return repository.findById(id);
    }

    public Store findByPostCode(String postCode) {
        return repository.findByPostCode(postCode);
    }

    public Result<Store> add(Store store) {
        Result<Store> result = validate(store);

        if(!result.isSuccess()) {
            return result;
        }

        if(store.getStoreId() != 0) {
            result.addMessage("ID must not be set for `add` operation",ResultType.INVALID);
        }

        for(Store s : repository.findAll()) {
            if(s.getPostalCode().equals(store.getPostalCode()) && s.getAddress().equalsIgnoreCase(store.getAddress())) {
                result.addMessage("This store already exists in the system", ResultType.INVALID);
            }
        }

        if(result.isSuccess()) {
            store = repository.add(store);
            result.setPayload(store);
        }
        return result;
    }


    public Result<Store> update(Store store) {
        Result<Store> result = validate(store);

        if(!result.isSuccess()) {
            return result;
        }

        if(store.getStoreId() <= 0) {
            result.addMessage("ID must be set for `update` operation",ResultType.INVALID);
        }

        for(Store s : repository.findAll()) {
            if(s.getStoreId() == store.getStoreId()) {
                continue;
            }
            if(s.getPostalCode().equals(store.getPostalCode()) && s.getAddress().equalsIgnoreCase(store.getAddress())) {
                result.addMessage("This store already exists in the system", ResultType.INVALID);
            }
        }

        if(!repository.update(store)) {
            String message = String.format("Store with ID number %s could not be found",store.getStoreId());
            result.addMessage(message,ResultType.NOT_FOUND);
        }
        return result;
    }


    public Result<Store> delete(int storeId) {
        Result<Store> result = new Result<>();
        if(!repository.delete(storeId)) {
            String message = String.format("Store with ID number %s could not be found",storeId);
            result.addMessage(message,ResultType.NOT_FOUND);
        }
        return result;
    }



    private Result<Store> validate(Store store) {
        Result<Store> result = new Result<>();

        if(store == null) {
            result.addMessage("Store cannot be null",ResultType.INVALID);
            return result;
        }

        if(store.getAddress() == null || store.getAddress().isBlank()) {
            result.addMessage("Address is required",ResultType.INVALID);
        }

        if(store.getCity() == null || store.getCity().isBlank()) {
            result.addMessage("City is required", ResultType.INVALID);
        }

        if(store.getState() == null || store.getState().isBlank()) {
            result.addMessage("State is required",ResultType.INVALID);
        }

        if(store.getPostalCode() == null || store.getPostalCode().isBlank()) {
            result.addMessage("Postal Code is required",ResultType.INVALID);
        }

        if(store.getPhone() == null || store.getPhone().isBlank()) {
            result.addMessage("Phone number is required",ResultType.INVALID);
        }

        return result;
    }

}
