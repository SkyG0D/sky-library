package sky.skygod.skylibrary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sky.skygod.skylibrary.exception.NotFoundException;
import sky.skygod.skylibrary.mapper.GenderMapper;
import sky.skygod.skylibrary.model.Gender;
import sky.skygod.skylibrary.repository.gender.GenderRepository;
import sky.skygod.skylibrary.dto.gender.GenderPostRequestBody;
import sky.skygod.skylibrary.dto.gender.GenderPutRequestBody;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GenderService {

    private final GenderRepository genderRepository;

    public List<Gender> list() {
        return genderRepository.findAll();
    }

    public Gender save(GenderPostRequestBody genderPostRequestBody) {
        return genderRepository.save(GenderMapper.INSTANCE.toGender(genderPostRequestBody));
    }

    public void delete(UUID uuid) {
        genderRepository.delete(findByIdOrElseThrowException(uuid));
    }

    public void replace(GenderPutRequestBody genderPutRequestBody) {
        Gender savedGender = findByIdOrElseThrowException(genderPutRequestBody.getUuid());
        Gender gender = genderRepository.save(GenderMapper.INSTANCE.toGender(genderPutRequestBody));
        gender.setUuid(savedGender.getUuid());
        genderRepository.save(gender);
    }

    private Gender findByIdOrElseThrowException(UUID uuid) {
        return genderRepository
                .findById(uuid)
                .orElseThrow(() -> new NotFoundException("Gender not found"));
    }

}
