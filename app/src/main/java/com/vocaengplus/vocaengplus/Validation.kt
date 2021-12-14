package com.vocaengplus.vocaengplus

class Validation {

    fun checkInput(input:Array<String>):Boolean{
        var flag=true
        input.forEach {
            if(it.length==0){
                flag=false
                return@forEach
            }
        }
        return flag
    }

    fun isValidateEmail(email:String):Boolean{
        val email_validation=Regex("^[a-zA-Z0-9]([-_.]?[a-zA-Z0-9])*@[a-zA-Z0-9]([-_.]?[a-zA-Z0-9])*.[a-zA-Z0-9]{2,3}$")
        if(email_validation.matches(email)){
            return true
        }
        return false
    }

    fun isValidateNickname(nickname:String):Boolean{
        val nickname_validation=Regex("^[a-zA-Z0-9가-힣]{2,10}$")
        if(nickname_validation.matches(nickname)){
            return true
        }
        return false
    }

    fun isValidatePassword(password:String):Boolean{
        val password_validation=Regex("^(?=.*[a-zA-Z])(?=.*[!@#\$%^*/])(?=.*[0-9]).{8,20}$")
        if(password_validation.matches(password)){
            return true
        }
        return false
    }

    fun isValidateWord(word:String):Boolean{
        val word_validation=Regex("^[a-zA-Z0-9][^가-힣@#$%^&*+/><=\\[\\]\\.\\\\]{1,39}$")
        if(word_validation.matches(word)){
            return true
        }
        return false
    }
    fun isValidateMeaning(meaning:String):Boolean{
        val meaning_validation=Regex("^[가-힣0-9][^a-zA-Z@#$%^&*+/><=\\\\\\[\\]]{0,39}$")
        if(meaning_validation.matches(meaning)){
            return true
        }
        return false
    }

    fun isValidateCategoryName(category:String):Boolean{
        val category_validation=Regex("^[가-힣A-Za-z0-9][^\\.#$\\[\\]]{1,19}$")
        if(category_validation.matches(category)){
            return true
        }
        return false
    }


}