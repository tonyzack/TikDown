package com.nadhholy.tikdownloader.video.models.error;


public enum ERRORS {

    INVALID_NUMBER(800,"Numéro invalide"),
    NUMBER_EXIST(100,"Number already registered"),
    NOT_FOUND(800,"Not found"),
    USER_NOT_FOUND(800,"User not found"),
    SENDER_NOT_FOUND(801,"Sender not found"),
    RECEIVER_NOT_FOUND(802,"Receiver not found"),
    NOT_CASHZONE_USER(803,"Le recuveur n'a pas de compte CashZone"),
    OPERATION_NOT_FOUND(116,"Operation non trouvée"),
    OPERATION_FINISHED(115,"Operation déjà effectuée"),
    OPERATION_WAITING(114,"Operation en cours"),
    UNAUTHORIZED_OPERATION(112,"Vous n'êtes pas autorisé à faire cette opération"),
    INAPPROPRIATE_OPERATION(113,"Opération inappropriée"),
    UNAUTHORIZED_ACTION(804,"Action non autorisées"),
    MISS_PARAMETERS(804,"miss parameters"),
    INVALID_CODE(804,"Code non valide"),
    INSUFFICIENT_AMOUNT(111,"Solde insuffisant pour cette operation"),
    INVALID_OPERATOR(805,"Invalid operator"),
    INVALID_TOKEN(151,"Invalid token"),
    MISSING_AUTHORIZATION(150,"missing Authorization header"),
    BANNED_ACCOUNT(332,"Banned Account"),
    UNAUTHORIZED_ACTION_ON_SHOP(806,"Banned Account"),
    UNAUTHORIZED_ACCESS_ON_SHOP(807,"Banned Account"),
    EXPIRED_ACTION(333,"Action Expired"),
    UNKNOWN_ERROR(888,"Sorry, something went wrong")
    ;

    private final int code;
    private final String name;
    private String message;

    ERRORS(int code, String message){
        this.code = code;
        this.message = message;
        this.name = this.name();
    }


    public final int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public ERRORS setMessage(String message) {
        this.message = message;
        return this;
    }

}
