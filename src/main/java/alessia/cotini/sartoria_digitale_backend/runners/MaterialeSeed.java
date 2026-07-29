package alessia.cotini.sartoria_digitale_backend.runners;

import java.util.List;

public record MaterialeSeed(String nome, Double prezzoAlMetro, List<ColoreSeed> colori) {}