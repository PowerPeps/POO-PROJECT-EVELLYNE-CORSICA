public class VariableHistorique {
    private String nomVariable;
    private String valeur;
    private String type;

    public VariableHistorique(String nomVariable, String valeur, String type) {
        this.nomVariable = nomVariable;
        this.valeur = valeur;
        this.type = type;
    }

    public void mettreAJourValeur(String valeur) {
        this.valeur = valeur;
    }

    public boolean verifierCondition(String nomVariable, String valeurAttendue) {
        return this.nomVariable.equals(nomVariable) && this.valeur.equals(valeurAttendue);
    }
}
