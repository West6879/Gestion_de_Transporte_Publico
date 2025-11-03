package estructura;

/*
Enum para los diferentes tipos de estación.
*/
public enum TipoEstacion {
    TREN("Tren"),
    METRO("Metro"),
    BUS("Bus");

    private final String displayName;
    TipoEstacion(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    // Metodo para convertir string a enum, por si es necesario.
    public static TipoEstacion fromString(String input) {
        if(input == null || input.trim().isEmpty()) return null;
        input = input.trim();

        for(TipoEstacion tipo : TipoEstacion.values()) {
            if(tipo.getDisplayName().equals(input)) {
                return tipo;
            }
        }

        try {
            String formato = input.toUpperCase().replace("-", "_").replace(" ", "_");
            return TipoEstacion.valueOf(formato);
        } catch(IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo invalido. " + input);
        }
    }
}
