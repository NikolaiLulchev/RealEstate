package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.OfferSeedRootDto;
import softuni.exam.models.entity.Offer;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.AgentService;
import softuni.exam.service.ApartmentService;
import softuni.exam.service.OfferService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {
    private static final String OFFERS_FILE_PATH = "src/main/resources/files/xml/offers.xml";

    private final OfferRepository offerRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final AgentService agentService;
    private final ApartmentService apartmentService;

    public OfferServiceImpl(OfferRepository offerRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser, AgentService agentService, ApartmentService apartmentService) {
        this.offerRepository = offerRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.agentService = agentService;

        this.apartmentService = apartmentService;
    }

    @Override
    public boolean areImported() {
        return offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(Path.of(OFFERS_FILE_PATH));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        xmlParser.fromFile(OFFERS_FILE_PATH, OfferSeedRootDto.class)
                .getOfferSeedDtos()
                .stream()
                .filter(offerSeedDto -> {
                    boolean isValid = validationUtil.isValid(offerSeedDto)
                                      && agentService.isEntityExist(offerSeedDto.getAgent().getName());


                    sb.append(isValid
                                    ? "Successfully imported offer " + offerSeedDto.getPrice()
                                    : "Invalid offer")
                            .append(System.lineSeparator());
                    return isValid;
                }).map(offerSeedDto ->
                {
                    Offer offer = modelMapper.map(offerSeedDto, Offer.class);
                    offer.setAgent(agentService.findByFirstName(offerSeedDto.getAgent().getName()));
                    offer.setApartment(apartmentService.findById(offerSeedDto.getApartment().getId()));
                    return offer;
                })
                .forEach(offerRepository::save);

        return sb.toString();
    }


    @Override
    public String exportOffers() {
        StringBuilder sb = new StringBuilder();

        List<Offer> allOffers = offerRepository.findAllByTypeThreeRoomsOrderedByAreaDescThenOrderedByPriceAsc();

        allOffers.forEach(offer -> {
            sb.append(String.format("Agent %s %s with offer №%d:\n" +
                                    "   \t\t-Apartment area: %.2f\n" +
                                    "   \t\t--Town: %s\n" +
                                    "   \t\t---Price: %.2f$",
                            offer.getAgent().getFirstName(),
                            offer.getAgent().getLastName(),
                            offer.getId(),
                            offer.getApartment().getArea(),
                            offer.getApartment().getTown().getTownName(),
                            offer.getPrice()
                    ))
                    .append(System.lineSeparator());


        });

        return sb.toString();
    }
}
