package gh.z0736190100.apparelstore.repositories;

import gh.z0736190100.apparelstore.entities.Apparel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ApparelRepositoryTest {

    @Autowired
    ApparelRepository apparelRepository;

    @Test
    void testSaveApparel() {
        // Given
        Apparel apparel = Apparel.builder()
                .apparelName("Test Apparel")
                .apparelStyle("Loose")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        // When
        Apparel savedApparel = apparelRepository.save(apparel);

        // Then
        assertThat(savedApparel).isNotNull();
        assertThat(savedApparel.getId()).isNotNull();
    }

    @Test
    void testGetApparelById() {
        // Given
        Apparel apparel = Apparel.builder()
                .apparelName("Test Apparel")
                .apparelStyle("Loose")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
        Apparel savedApparel = apparelRepository.save(apparel);

        // When
        Optional<Apparel> fetchedApparelOptional = apparelRepository.findById(savedApparel.getId());

        // Then
        assertThat(fetchedApparelOptional).isPresent();
        Apparel fetchedApparel = fetchedApparelOptional.get();
        assertThat(fetchedApparel.getApparelName()).isEqualTo("Test Apparel");
    }

    @Test
    void testUpdateApparel() {
        // Given
        Apparel apparel = Apparel.builder()
                .apparelName("Original Name")
                .apparelStyle("Loose")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
        Apparel savedApparel = apparelRepository.save(apparel);

        // When
        savedApparel.setApparelName("Updated Name");
        Apparel updatedApparel = apparelRepository.save(savedApparel);

        // Then
        assertThat(updatedApparel.getApparelName()).isEqualTo("Updated Name");
    }

    @Test
    void testDeleteApparel() {
        // Given
        Apparel apparel = Apparel.builder()
                .apparelName("Delete Me")
                .apparelStyle("Oversize")
                .upc("654321")
                .price(new BigDecimal("9.99"))
                .quantityOnHand(50)
                .build();
        Apparel savedApparel = apparelRepository.save(apparel);

        // When
        apparelRepository.deleteById(savedApparel.getId());
        Optional<Apparel> deletedApparel = apparelRepository.findById(savedApparel.getId());

        // Then
        assertThat(deletedApparel).isEmpty();
    }

    @Test
    void testListApparels() {
        // Given
        apparelRepository.deleteAll(); // Clear any existing data
        Apparel apparel1 = Apparel.builder()
                .apparelName("Apparel 1")
                .apparelStyle("Loose")
                .upc("111111")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(100)
                .build();
        Apparel apparel2 = Apparel.builder()
                .apparelName("Apparel 2")
                .apparelStyle("Fit")
                .upc("222222")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(200)
                .build();
        apparelRepository.saveAll(List.of(apparel1, apparel2));

        // When
        List<Apparel> apparels = apparelRepository.findAll();

        // Then
        assertThat(apparels).hasSize(2);
    }

    @Test
    void testFindAllByApparelNameContainingIgnoreCaseWithPagination() {
        // Given
        apparelRepository.deleteAll(); // Clear any existing data
        Apparel apparel1 = Apparel.builder()
                .apparelName("Test Apparel")
                .apparelStyle("Loose")
                .upc("111111")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(100)
                .build();
        Apparel apparel2 = Apparel.builder()
                .apparelName("Another Test Apparel")
                .apparelStyle("Fit")
                .upc("222222")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(200)
                .build();
        Apparel apparel3 = Apparel.builder()
                .apparelName("Not Matching")
                .apparelStyle("Oversize")
                .upc("333333")
                .price(new BigDecimal("10.99"))
                .quantityOnHand(150)
                .build();
        apparelRepository.saveAll(List.of(apparel1, apparel2, apparel3));

        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Apparel> apparelsPage = apparelRepository.findAllByApparelNameContainingIgnoreCase("Test", pageable);

        // Then
        assertThat(apparelsPage.getContent()).hasSize(2);
        assertThat(apparelsPage.getTotalElements()).isEqualTo(2);
        assertThat(apparelsPage.getContent().get(0).getApparelName()).contains("Test");
        assertThat(apparelsPage.getContent().get(1).getApparelName()).contains("Test");
    }

    @Test
    void testFindAllByApparelNameContainingIgnoreCaseWithEmptyString() {
        // Given
        apparelRepository.deleteAll(); // Clear any existing data
        Apparel apparel1 = Apparel.builder()
                .apparelName("Apparel 1")
                .apparelStyle("Loose")
                .upc("111111")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(100)
                .build();
        Apparel apparel2 = Apparel.builder()
                .apparelName("Apparel 2")
                .apparelStyle("Fit")
                .upc("222222")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(200)
                .build();
        apparelRepository.saveAll(List.of(apparel1, apparel2));

        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Apparel> apparelsPage = apparelRepository.findAllByApparelNameContainingIgnoreCase("", pageable);

        // Then
        assertThat(apparelsPage.getContent()).hasSize(2);
        assertThat(apparelsPage.getTotalElements()).isEqualTo(2);
    }

    @Test
    void testFindAllByApparelNameContainingIgnoreCasePagination() {
        // Given
        apparelRepository.deleteAll(); // Clear any existing data
        // Create 25 apparels with "Test" in the name
        for (int i = 1; i <= 25; i++) {
            Apparel apparel = Apparel.builder()
                    .apparelName("Test Apparel " + i)
                    .apparelStyle(i % 2 == 0 ? "Loose" : "Fit")
                    .upc("1111" + i)
                    .price(new BigDecimal("11.99"))
                    .quantityOnHand(100 + i)
                    .build();
            apparelRepository.save(apparel);
        }

        // First page (0-based index)
        Pageable firstPageable = PageRequest.of(0, 10);
        Page<Apparel> firstPage = apparelRepository.findAllByApparelNameContainingIgnoreCase("Test", firstPageable);

        // Second page
        Pageable secondPageable = PageRequest.of(1, 10);
        Page<Apparel> secondPage = apparelRepository.findAllByApparelNameContainingIgnoreCase("Test", secondPageable);

        // Third page (should have only 5 items)
        Pageable thirdPageable = PageRequest.of(2, 10);
        Page<Apparel> thirdPage = apparelRepository.findAllByApparelNameContainingIgnoreCase("Test", thirdPageable);

        // Then
        assertThat(firstPage.getContent()).hasSize(10);
        assertThat(firstPage.getNumber()).isEqualTo(0);
        assertThat(firstPage.getTotalElements()).isEqualTo(25);
        assertThat(firstPage.getTotalPages()).isEqualTo(3);

        assertThat(secondPage.getContent()).hasSize(10);
        assertThat(secondPage.getNumber()).isEqualTo(1);

        assertThat(thirdPage.getContent()).hasSize(5);
        assertThat(thirdPage.getNumber()).isEqualTo(2);
    }

    @Test
    void testFindAllByApparelNameAndApparelStyleContainingIgnoreCase() {
        // Given
        apparelRepository.deleteAll(); // Clear any existing data
        Apparel apparel1 = Apparel.builder()
                .apparelName("Test Loose")
                .apparelStyle("Loose")
                .upc("111111")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(100)
                .build();
        Apparel apparel2 = Apparel.builder()
                .apparelName("Test Fit")
                .apparelStyle("Fit")
                .upc("222222")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(200)
                .build();
        Apparel apparel3 = Apparel.builder()
                .apparelName("Another Loose")
                .apparelStyle("Loose")
                .upc("333333")
                .price(new BigDecimal("10.99"))
                .quantityOnHand(150)
                .build();
        Apparel apparel4 = Apparel.builder()
                .apparelName("Not Matching")
                .apparelStyle("Oversize")
                .upc("444444")
                .price(new BigDecimal("9.99"))
                .quantityOnHand(120)
                .build();
        apparelRepository.saveAll(List.of(apparel1, apparel2, apparel3, apparel4));

        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Apparel> apparelsPage = apparelRepository.findAllByApparelNameContainingIgnoreCaseAndApparelStyleContainingIgnoreCase("Test", "Loose", pageable);

        // Then
        assertThat(apparelsPage.getContent()).hasSize(1);
        assertThat(apparelsPage.getTotalElements()).isEqualTo(1);
        assertThat(apparelsPage.getContent().get(0).getApparelName()).isEqualTo("Test Loose");
        assertThat(apparelsPage.getContent().get(0).getApparelStyle()).isEqualTo("Loose");
    }

    @Test
    void testFindAllByApparelNameAndApparelStyleContainingIgnoreCaseWithEmptyApparelName() {
        // Given
        apparelRepository.deleteAll(); // Clear any existing data
        Apparel apparel1 = Apparel.builder()
                .apparelName("Test Loose")
                .apparelStyle("Loose")
                .upc("111111")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(100)
                .build();
        Apparel apparel2 = Apparel.builder()
                .apparelName("Another Loose")
                .apparelStyle("Loose")
                .upc("222222")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(200)
                .build();
        Apparel apparel3 = Apparel.builder()
                .apparelName("Test Fit")
                .apparelStyle("Fit")
                .upc("333333")
                .price(new BigDecimal("10.99"))
                .quantityOnHand(150)
                .build();
        apparelRepository.saveAll(List.of(apparel1, apparel2, apparel3));

        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Apparel> apparelsPage = apparelRepository.findAllByApparelNameContainingIgnoreCaseAndApparelStyleContainingIgnoreCase("", "Loose", pageable);

        // Then
        assertThat(apparelsPage.getContent()).hasSize(2);
        assertThat(apparelsPage.getTotalElements()).isEqualTo(2);
        assertThat(apparelsPage.getContent().get(0).getApparelStyle()).isEqualTo("Loose");
        assertThat(apparelsPage.getContent().get(1).getApparelStyle()).isEqualTo("Loose");
    }

    @Test
    void testFindAllByApparelNameAndApparelStyleContainingIgnoreCaseWithEmptyApparelStyle() {
        // Given
        apparelRepository.deleteAll(); // Clear any existing data
        Apparel apparel1 = Apparel.builder()
                .apparelName("Test Loose")
                .apparelStyle("Loose")
                .upc("111111")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(100)
                .build();
        Apparel apparel2 = Apparel.builder()
                .apparelName("Test Fit")
                .apparelStyle("Fit")
                .upc("222222")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(200)
                .build();
        Apparel apparel3 = Apparel.builder()
                .apparelName("Another Apparel")
                .apparelStyle("Oversize")
                .upc("333333")
                .price(new BigDecimal("10.99"))
                .quantityOnHand(150)
                .build();
        apparelRepository.saveAll(List.of(apparel1, apparel2, apparel3));

        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Apparel> apparelsPage = apparelRepository.findAllByApparelNameContainingIgnoreCaseAndApparelStyleContainingIgnoreCase("Test", "", pageable);

        // Then
        assertThat(apparelsPage.getContent()).hasSize(2);
        assertThat(apparelsPage.getTotalElements()).isEqualTo(2);
        assertThat(apparelsPage.getContent().get(0).getApparelName()).contains("Test");
        assertThat(apparelsPage.getContent().get(1).getApparelName()).contains("Test");
    }

    @Test
    void testFindAllByApparelNameAndApparelStyleContainingIgnoreCaseWithBothEmpty() {
        // Given
        apparelRepository.deleteAll(); // Clear any existing data
        Apparel apparel1 = Apparel.builder()
                .apparelName("Test IPA")
                .apparelStyle("IPA")
                .upc("111111")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(100)
                .build();
        Apparel apparel2 = Apparel.builder()
                .apparelName("Another Apparel")
                .apparelStyle("Fit")
                .upc("222222")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(200)
                .build();
        apparelRepository.saveAll(List.of(apparel1, apparel2));

        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Apparel> apparelsPage = apparelRepository.findAllByApparelNameContainingIgnoreCaseAndApparelStyleContainingIgnoreCase("", "", pageable);

        // Then
        assertThat(apparelsPage.getContent()).hasSize(2);
        assertThat(apparelsPage.getTotalElements()).isEqualTo(2);
    }
}
