from rest_framework import serializers, response, validators
from .models import Wallet, Transfer, transfer_code, Credit
from rest_framework.validators import UniqueTogetherValidator
from datetime import datetime, date
from dateutil.relativedelta import relativedelta
from rest_framework.exceptions import ValidationError


class WalletSerializer(serializers.ModelSerializer):
    wallet_currency = serializers.CharField(default='KGZ', read_only=True, write_only=False)

    class Meta:
        model = Wallet
        fields = (
            'user',
            'wallet_currency',
            'wallet',
            'is_active',
        )
        extra_kwargs = {
            'wallet': {'read_only': True},
            'is_active': {'read_only': True},
        }

        validators = [
            UniqueTogetherValidator(
                queryset=Wallet.objects.all(),
                fields=['user']
            )
        ]


currencies = (
    ('USD', 'USD'),
    ('EUR', 'EUR'),
    ('KGS', 'KGZ'),
)

class ChangeCurrencySerializer(serializers.Serializer):
    currency = serializers.ChoiceField(choices=currencies)


class TransferSerializer(serializers.ModelSerializer):
    code = serializers.IntegerField(default=transfer_code, read_only=True)

    class Meta:
        model = Transfer
        fields = (
            'code',
            'sender',
            'receiver',
            'sum',
            'currency',
        )


class CreditSerializer(serializers.ModelSerializer):
    j_check = serializers.BooleanField(default=False, write_only=True)
    class Meta:
        model = Credit
        fields = (
            'j_check',
            'identification_number',
            'user',
            'salary',
            'pledge',
            'price',
            'final_amount',
            'paid',
            'credit_long',
            'date_taking',
            'date_payment',
            'every_month',
            'notices',
            'approval',
        )
        extra_kwargs = {
            'date_taking': {'read_only': True},
            'identification_number': {'read_only': True},
            'every_month': {'read_only': True},
            'date_payment': {'read_only': True},
            'final_amount': {'read_only': True},
            'notices': {'read_only': True},
            'approval': {'read_only': True},

        }

    def validate(self, validated_data):
        salary = validated_data['salary']
        pledge = validated_data['pledge']
        price = validated_data['price']
        if (70 <= salary and 300 <= price <= 10000 and pledge == "consumer") or \
            (1500 <= salary and 10000 <= price <= 1000000 and pledge == "mortgage"):
            return validated_data

        raise ValidationError({"Status": "Error", "Message": "Please check rules how to full the blank"})
