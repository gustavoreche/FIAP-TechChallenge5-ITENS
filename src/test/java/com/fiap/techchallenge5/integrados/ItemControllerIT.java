package com.fiap.techchallenge5.integrados;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge5.infrastructure.item.controller.dto.AtualizaItemDTO;
import com.fiap.techchallenge5.infrastructure.item.controller.dto.CriaItemDTO;
import com.fiap.techchallenge5.infrastructure.item.model.ItemEntity;
import com.fiap.techchallenge5.infrastructure.item.repository.ItemRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static com.fiap.techchallenge5.infrastructure.item.controller.ItemController.URL_ITEM;
import static com.fiap.techchallenge5.infrastructure.item.controller.ItemController.URL_ITEM_COM_EAN;


@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ItemControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String token = JwtUtil.geraJwt();

    @BeforeEach
    void inicializaLimpezaDoDatabase() {
        this.itemRepository.deleteAll();
    }

    @AfterAll
    void finalizaLimpezaDoDatabase() {
        this.itemRepository.deleteAll();
    }

    @Test
    public void cadastra_deveRetornar201_salvaNaBaseDeDados() throws Exception {

        var request = new CriaItemDTO(
                7894900011517L,
                "Item Teste",
                new BigDecimal("100"),
                100L
        );
        var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        var jsonRequest = objectMapper.writeValueAsString(request);

        this.mockMvc
                .perform(MockMvcRequestBuilders.post(URL_ITEM)
                        .header("Authorization", "Bearer " + this.token)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isCreated()
                )
                .andReturn();

        var item = this.itemRepository.findAll().get(0);

        Assertions.assertEquals(7894900011517L, item.getEan());
        Assertions.assertEquals("Item Teste", item.getNome());
        Assertions.assertEquals(new BigDecimal("100.00"), item.getPreco());
        Assertions.assertEquals(100L, item.getQuantidade());
        Assertions.assertNotNull(item.getDataDeCriacao());
    }

    @Test
    public void cadastra_deveRetornar409_naoSalvaNaBaseDeDados() throws Exception {

        this.itemRepository.save(ItemEntity.builder()
                .ean(7894900011517L)
                .nome("Item Teste")
                .preco(new BigDecimal("100"))
                .quantidade(100L)
                .dataDeCriacao(LocalDateTime.now())
                .build());

        var request = new CriaItemDTO(
                7894900011517L,
                "Item Teste",
                new BigDecimal("100"),
                100L
        );
        var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        var jsonRequest = objectMapper.writeValueAsString(request);

        this.mockMvc
                .perform(MockMvcRequestBuilders.post(URL_ITEM)
                        .header("Authorization", "Bearer " + this.token)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isConflict()
                )
                .andReturn();

        var item = this.itemRepository.findAll().get(0);

        Assertions.assertEquals(1, this.itemRepository.findAll().size());
        Assertions.assertEquals(7894900011517L, item.getEan());
        Assertions.assertEquals("Item Teste", item.getNome());
        Assertions.assertEquals(new BigDecimal("100.00"), item.getPreco());
        Assertions.assertEquals(100L, item.getQuantidade());
        Assertions.assertNotNull(item.getDataDeCriacao());
    }

    @Test
    public void atualiza_deveRetornar200_salvaNaBaseDeDados() throws Exception {

        this.itemRepository.save(ItemEntity.builder()
                .ean(7894900011517L)
                .nome("Item Teste")
                .preco(new BigDecimal("100"))
                .quantidade(100L)
                .dataDeCriacao(LocalDateTime.now())
                .build());

        var request = new AtualizaItemDTO(
                "Item Teste",
                new BigDecimal("95"),
                150L
        );
        var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        var jsonRequest = objectMapper.writeValueAsString(request);

        this.mockMvc
                .perform(MockMvcRequestBuilders.put(URL_ITEM_COM_EAN.replace("{ean}", "7894900011517"))
                        .header("Authorization", "Bearer " + this.token)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk()
                )
                .andReturn();

        var item = this.itemRepository.findAll().get(0);

        Assertions.assertEquals(7894900011517L, item.getEan());
        Assertions.assertEquals("Item Teste", item.getNome());
        Assertions.assertEquals(new BigDecimal("95.00"), item.getPreco());
        Assertions.assertEquals(250L, item.getQuantidade());
        Assertions.assertNotNull(item.getDataDeCriacao());
    }

    @Test
    public void atualiza_deveRetornar204_naoSalvaNaBaseDeDados() throws Exception {

        var request = new AtualizaItemDTO(
                "Item Teste",
                new BigDecimal("100"),
                100L
        );
        var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        var jsonRequest = objectMapper.writeValueAsString(request);

        this.mockMvc
                .perform(MockMvcRequestBuilders.put(URL_ITEM_COM_EAN.replace("{ean}", "7894900011517"))
                        .header("Authorization", "Bearer " + this.token)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNoContent()
                )
                .andReturn();

        Assertions.assertEquals(0, this.itemRepository.findAll().size());
    }

    @Test
    public void deleta_deveRetornar200_deletaNaBaseDeDados() throws Exception {

        this.itemRepository.save(ItemEntity.builder()
                .ean(7894900011517L)
                .nome("Item Teste")
                .preco(new BigDecimal("100"))
                .quantidade(100L)
                .dataDeCriacao(LocalDateTime.now())
                .build());

        this.mockMvc
                .perform(MockMvcRequestBuilders.delete(URL_ITEM_COM_EAN.replace("{ean}", "7894900011517"))
                        .header("Authorization", "Bearer " + this.token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk()
                )
                .andReturn();

        Assertions.assertEquals(0, this.itemRepository.findAll().size());
    }

    @Test
    public void deleta_deveRetornar204_naoDeletaNaBaseDeDados() throws Exception {

        this.mockMvc
                .perform(MockMvcRequestBuilders.delete(URL_ITEM_COM_EAN.replace("{ean}", "7894900011517"))
                        .header("Authorization", "Bearer " + this.token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNoContent()
                )
                .andReturn();

        Assertions.assertEquals(0, this.itemRepository.findAll().size());
    }

    @Test
    public void busca_deveRetornar200_buscaNaBaseDeDados() throws Exception {

        this.itemRepository.save(ItemEntity.builder()
                .ean(7894900011517L)
                .nome("Item Teste")
                .preco(new BigDecimal("100"))
                .quantidade(100L)
                .dataDeCriacao(LocalDateTime.now())
                .build());

        var response = this.mockMvc
                .perform(MockMvcRequestBuilders.get(URL_ITEM_COM_EAN.replace("{ean}", "7894900011517"))
                        .header("Authorization", "Bearer " + this.token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk()
                )
                .andReturn();
        var responseAppString = response.getResponse().getContentAsString();
        var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<ItemEntity>() {});

        var item = this.itemRepository.findAll().get(0);

        Assertions.assertEquals(1, this.itemRepository.findAll().size());
        Assertions.assertEquals(responseApp.getEan(), item.getEan());
        Assertions.assertEquals(responseApp.getNome(), item.getNome());
        Assertions.assertEquals(responseApp.getPreco(), item.getPreco());
        Assertions.assertEquals(responseApp.getQuantidade(), item.getQuantidade());
        Assertions.assertEquals(responseApp.getDataDeCriacao(), item.getDataDeCriacao());
    }

    @Test
    public void busca_deveRetornar204_naoEcontraNaBaseDeDados() throws Exception {

        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URL_ITEM_COM_EAN.replace("{ean}", "7894900011517"))
                        .header("Authorization", "Bearer " + this.token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNoContent()
                )
                .andReturn();

        Assertions.assertEquals(0, this.itemRepository.findAll().size());
    }

    @ParameterizedTest
    @MethodSource("requestValidandoCampos")
    public void cadastra_camposInvalidos_naoBuscaNaBaseDeDados(Long ean,
                                                               String nome,
                                                               BigDecimal preco,
                                                               Long quantidade) throws Exception {
        var request = new CriaItemDTO(
                ean,
                nome,
                preco,
                quantidade
        );
        var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        var jsonRequest = objectMapper.writeValueAsString(request);

        this.mockMvc
                .perform(MockMvcRequestBuilders.post(URL_ITEM)
                        .header("Authorization", "Bearer " + this.token)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest()
                );
    }

    @ParameterizedTest
    @MethodSource("requestValidandoCampos")
    public void atualiza_camposInvalidos_naoBuscaNaBaseDeDados(Long ean,
                                                               String nome,
                                                               BigDecimal preco,
                                                               Long quantidade) throws Exception {
        var request = new AtualizaItemDTO(
                nome,
                preco,
                quantidade
        );
        var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        var jsonRequest = objectMapper.writeValueAsString(request);

        this.mockMvc
                .perform(MockMvcRequestBuilders.put(URL_ITEM_COM_EAN.replace("{ean}", ean.toString()))
                        .header("Authorization", "Bearer " + this.token)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest()
                );
    }

    @ParameterizedTest
    @ValueSource(longs = {
            -1L,
            0
    })
    public void deleta_camposInvalidos_naoDeletaNaBaseDeDados(Long ean) throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete(URL_ITEM_COM_EAN.replace("{ean}", ean.toString()))
                        .header("Authorization", "Bearer " + this.token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest()
                );
    }

    @ParameterizedTest
    @ValueSource(longs = {
            -1L,
            0
    })
    public void busca_camposInvalidos_naoBuscaNaBaseDeDados(Long ean) throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URL_ITEM_COM_EAN.replace("{ean}", ean.toString()))
                        .header("Authorization", "Bearer " + this.token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest()
                );
    }

    private static Stream<Arguments> requestValidandoCampos() {
        return Stream.of(
                Arguments.of(-1L, "Nome de teste", new BigDecimal("100"), 100L),
                Arguments.of(0L, "Nome de teste", new BigDecimal("100"), 100L),
                Arguments.of(123456789L, null, new BigDecimal("100"), 100L),
                Arguments.of(123456789L, "", new BigDecimal("100"), 100L),
                Arguments.of(123456789L, " ", new BigDecimal("100"), 100L),
                Arguments.of(123456789L, "ab", new BigDecimal("100"), 100L),
                Arguments.of(123456789L, "aaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbbaaaaaaaaaab", new BigDecimal("100"), 100L),
                Arguments.of(123456789L, "Nome de teste", BigDecimal.ZERO, 100L),
                Arguments.of(123456789L, "Nome de teste", new BigDecimal("-1"), 100L),
                Arguments.of(123456789L, "Nome de teste", new BigDecimal("100"), -1L),
                Arguments.of(123456789L, "Nome de teste", new BigDecimal("100"), 0L),
                Arguments.of(123456789L, "Nome de teste", new BigDecimal("100"), 1001L)
        );
    }

}
