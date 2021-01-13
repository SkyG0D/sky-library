package sky.skygod.skylibrary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sky.skygod.skylibrary.model.Gender;
import sky.skygod.skylibrary.repository.GenderRepository;

@Service
@RequiredArgsConstructor
public class GenderService {

    private final GenderRepository genderRepository;

    public Gender save(Gender gender) {
        return genderRepository.save(gender);
    }

}
