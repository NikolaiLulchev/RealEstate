package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ApartmentSeedRootDto;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.ApartmentService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ApartmentServiceImpl implements ApartmentService {
    private static final String APARTMENTS_FILE_PATH = "src/main/resources/files/xml/apartments.xml";

    private final ApartmentRepository apartmentRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final TownRepository townRepository;

    public ApartmentServiceImpl(ApartmentRepository apartmentRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser, TownRepository townRepository) {
        this.apartmentRepository = apartmentRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.townRepository = townRepository;
    }

    @Override
    public boolean areImported() {
        return apartmentRepository.count() > 0;
    }

    @Override
    public String readApartmentsFromFile() throws IOException {
        return Files.readString(Path.of(APARTMENTS_FILE_PATH));
    }

    @Override
    public String importApartments() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        xmlParser.fromFile(APARTMENTS_FILE_PATH, ApartmentSeedRootDto.class)
                .getApartmentSeedDtos()
                .stream()
                .filter(apartmentSeedDto -> {
                    boolean isValid = validationUtil.isValid(apartmentSeedDto)
                                      && !isEntityExist(apartmentSeedDto.getTown(), apartmentSeedDto.getArea());
                    sb
                            .append(isValid
                                    ? String.format("Successfully imported apartment %s - %.2f"
                                    , apartmentSeedDto.getApartmentType(), apartmentSeedDto.getArea())
                                    : "Invalid apartment")
                            .append(System.lineSeparator());
                    return isValid;
                }).map(apartmentSeedDto -> {
                    Apartment apartment = modelMapper.map(apartmentSeedDto, Apartment.class);
                    apartment.setTown(townRepository.findByTownName(apartmentSeedDto.getTown()));
                    return apartment;
                })
                .forEach(apartmentRepository::save);


        return sb.toString();
    }

    @Override
    public Apartment findById(Long apartmentId) {
        return apartmentRepository.findById((long) apartmentId).orElse(null);
    }

    private boolean isEntityExist(String townName, double area) {
        Town town = townRepository.findByTownName(townName);
        return apartmentRepository.existsByTownAndArea(town, area);
    }

}
