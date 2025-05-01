package com.example.newApp.service;

import com.example.newApp.dto.SupplierDTO;
import com.example.newApp.dto.ItemDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataService {

    private final RestTemplate restTemplate;

    // Inject RestTemplate via constructor
    public DataService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Méthode principale pour récupérer les fournisseurs et articles depuis l'API.
     * @return Map contenant les fournisseurs et articles.
     */
    public Map<String, List<?>> getSuppliersAndItems() {
        // Récupérer les données depuis l'API
        Map<String, Object> response = fetchDataFromApi();

        if (response == null) {
            return null;
        }

        // Extraire et convertir les fournisseurs et articles
        List<SupplierDTO> suppliers = extractSuppliers(response);
        List<ItemDTO> items = extractItems(response);

        // Retourner un map avec les deux listes
        return Map.of("suppliers", suppliers, "items", items);
    }

    /**
     * Fonction pour récupérer les données brutes de l'API.
     * @return Map contenant les données.
     */
    private Map<String, Object> fetchDataFromApi() {
        String apiUrl = "http://erpnext.localhost:8000/api/method/getAllData";

        // Construire l'URI
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl).toUriString();

        try {
            // Effectuer la requête GET pour obtenir les données
            return restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            // Gérer les erreurs
            System.err.println("Erreur lors de la récupération des données depuis l'API : " + e.getMessage());
            return null;
        }
    }

    /**
     * Extraire et convertir les données des fournisseurs.
     * @param response La réponse de l'API sous forme de Map.
     * @return Liste de DTO de fournisseurs.
     */
    private List<SupplierDTO> extractSuppliers(Map<String, Object> response) {
        // Extraire l'objet "message"
        Map<String, Object> message = (Map<String, Object>) response.get("message");

        // Vérifier si "suppliers" est présent dans la réponse et le convertir
        if (message.containsKey("suppliers")) {
            List<Map<String, Object>> suppliersData = (List<Map<String, Object>>) message.get("suppliers");

            return suppliersData.stream()
                    .map(this::convertToSupplierDTO)
                    .collect(Collectors.toList());
        } else {
            return List.of(); // Retourner une liste vide si "suppliers" n'existe pas
        }
    }

    /**
     * Convertir une map de données de fournisseur en DTO.
     * @param supplierData Les données d'un fournisseur.
     * @return DTO de fournisseur.
     */
    private SupplierDTO convertToSupplierDTO(Map<String, Object> supplierData) {
        SupplierDTO dto = new SupplierDTO();
        dto.setName((String) supplierData.get("name"));
        dto.setCreation((String) supplierData.get("creation"));
        dto.setModified((String) supplierData.get("modified"));
        dto.setSupplierName((String) supplierData.get("supplier_name"));
        dto.setCountry((String) supplierData.get("country"));
        dto.setSupplierType((String) supplierData.get("supplier_type"));
        return dto;
    }

    /**
     * Extraire et convertir les données des articles.
     * @param response La réponse de l'API sous forme de Map.
     * @return Liste de DTO d'articles.
     */
    private List<ItemDTO> extractItems(Map<String, Object> response) {
        // Extraire l'objet "message"
        Map<String, Object> message = (Map<String, Object>) response.get("message");

        // Vérifier si "items" est présent dans la réponse et le convertir
        if (message.containsKey("items")) {
            List<Map<String, Object>> itemsData = (List<Map<String, Object>>) message.get("items");

            return itemsData.stream()
                    .map(this::convertToItemDTO)
                    .collect(Collectors.toList());
        } else {
            return List.of(); // Retourner une liste vide si "items" n'existe pas
        }
    }

    /**
     * Convertir une map de données d'article en DTO.
     * @param itemData Les données d'un article.
     * @return DTO d'article.
     */
    private ItemDTO convertToItemDTO(Map<String, Object> itemData) {
        ItemDTO dto = new ItemDTO();
        dto.setName((String) itemData.get("name"));
        dto.setItemCode((String) itemData.get("item_code"));
        dto.setItemName((String) itemData.get("item_name"));
        dto.setItemGroup((String) itemData.get("item_group"));
        dto.setStockUom((String) itemData.get("stock_uom"));
        dto.setLastPurchaseRate((Double) itemData.get("last_purchase_rate"));
        dto.setCountryOfOrigin((String) itemData.get("country_of_origin"));
        dto.setIsPurchaseItem((Integer) itemData.get("is_purchase_item") == 1);
        return dto;
    }
}
