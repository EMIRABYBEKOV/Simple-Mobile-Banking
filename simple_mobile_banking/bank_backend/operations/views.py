from account.models import Account
from rest_framework import viewsets,response, status, permissions, mixins, generics
from . import serializers
from .models import Wallet, Transfer, send, Credit, count_credit
from . import utils
from .currency_parser import get_currency


class CreateWallet(viewsets.ModelViewSet):
    serializer_class = serializers.WalletSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get(self, request, *args, **kwargs):
        user = request.user
        try:
            w = Wallet.objects.get(user=user.pk)

        except:
            return response.Response(
                {
                    'Status': 'Fail',
                    'Message': 'This account does not have a wallet',
                },
                status=status.HTTP_404_NOT_FOUND
            )
        wallet = serializers.WalletSerializer(w)
        return response.Response(
            {
                'Status': 'Success',
                'data': wallet.data
            },
            status=status.HTTP_200_OK
        )

    def create(self, request, *args, **kwargs):
        serializer = self.serializer_class(data=request.data)

        if serializer.is_valid():
            if Account.objects.get(pk=int(serializer.data['user'])).verification is True:
                account = Account.objects.get(pk=int(serializer.data['user']))
                # if account.cards >= 3:
                print(Account.username)
                wallet = Wallet(user=account)
                wallet.save()
                cards = account.cards
                Account.objects.filter(pk=int(serializer.data['user'])).update(cadrs=cards+1)
                return response.Response(
                    {
                        'Status': 'Success',
                        'Data': serializer.data
                    },
                    status=status.HTTP_200_OK
                )
            #     return response.Response({
            #         'Status': 'Fail',
            #         'Message': "User can create just 3 cards",
            #         'Data': []
            # })
            return response.Response({
                'Status': 'Fail',
                'Message': "User has to verificate his email",
                'Data': []
            })
        else:
            return response.Response(
                {
                    'Status': 'Fail',
                    'Message': serializer.errors,
                    'Data': []
                }
            )


class ChangeCurrency(viewsets.ModelViewSet):
    serializer_class = serializers.ChangeCurrencySerializer
    permission_classes = [permissions.IsAuthenticated]
    def post(self, request):
        serializer = self.serializer_class(data=request.data)
        if serializer.is_valid():
            user = request.user
            if user.verification == True:
                currency = serializer.data['currency']
                result = utils.change_currency(user.pk, currency)
                return response.Response(
                    {
                        'Status': 'Success',
                        'Data': result
                    },
                    status=status.HTTP_200_OK
                )
            return response.Response({
                'Status': 'Fail',
                'Message': "User has to verificate his email",
                'Data': []
            })
        return response.Response(
            {
                'Status': 'Fail',
                'Message': serializer.errors,
                'Data': []
            }
        )


class TransferView(viewsets.ModelViewSet):
    serializer_class = serializers.TransferSerializer
    permission_classes = [permissions.IsAuthenticated]
    queryset = Transfer.objects.all()

    def post(self, request):
        serializer = self.serializer_class(data=request.data)
        if serializer.is_valid():
            user = request.user
            if user.verification:
                if Account.objects.filter(pk=serializer.data['sender']).exists():
                    if Account.objects.filter(pk=int(serializer.data['receiver'])).exists():
                        receiver = Account.objects.get(pk=int(serializer.data['receiver']))
                        sender = Account.objects.get(pk=int(serializer.data['sender']))
                        if receiver != sender:
                            if Wallet.objects.filter(user=receiver.pk).exists():
                                if Wallet.objects.filter(user=sender.pk).exists():
                                    if serializer.data['currency'] == Wallet.objects.get(user=sender.pk).wallet_currency:
                                            if Wallet.objects.get(user=sender.pk).wallet >= float(serializer.data['sum']):

                                                wallet_s = float(Wallet.objects.get(user=sender.pk).wallet)
                                                wallet_s -= float(serializer.data['sum'])
                                                Wallet.objects.filter(user=sender.pk).update(wallet=wallet_s)

                                                wallet_r = Wallet.objects.get(user=receiver.pk)
                                                if wallet_r.wallet_currency == serializer.data['currency']:
                                                    sum = float(wallet_r.wallet)
                                                    sum += float(serializer.data['sum'])
                                                    Wallet.objects.filter(user=receiver.pk).update(wallet=sum)
                                                    send(serializer.data['code'], serializer.data['sender'],
                                                         serializer.data['receiver'], float(serializer.data['sum']), serializer.data['currency'])
                                                    return response.Response({"message": f"Success"})
                                                change = get_currency(serializer.data['currency'], wallet_r.wallet_currency)
                                                sum = float(serializer.data['sum'])
                                                sum *= float(change)
                                                l_sum = Wallet.objects.get(user=receiver.pk).wallet
                                                Wallet.objects.filter(user=receiver.pk).update(wallet=sum+float(l_sum))
                                                send(serializer.data['code'], serializer.data['sender'],
                                                     serializer.data['receiver'], float(serializer.data['sum']),
                                                     serializer.data['currency'])
                                                return response.Response({"message": f"receiver became money in changed"
                                                                                     f" currency as he has another currency"})
                                            return response.Response({"message": f"Sender with identification number {sender.pk}"
                                                                             f" doesn't have enough money"})
                                    return response.Response({'message': "Your corrency is different to "
                                                                         "currency that you want to send. You have to change it"})
                                return response.Response({'message': "sender doesn't have a Wallet"})
                            return response.Response({'message': "receiver doesn't have a Wallet"})
                        return response.Response({'message': "You can't send money to your self"})
                    return response.Response({'message': 'receiver does not have an account'})
                return response.Response({'message': "sender does not have an account"})
            return response.Response({
                'Status': 'Error',
                'Message': 'Verification credentials were not provided'
            })
        return response.Response({
            'Status': 'Error',
            'Message': 'data is not valid'
        })


class TakeCreditView(viewsets.GenericViewSet, mixins.CreateModelMixin, mixins.ListModelMixin):
    queryset = Credit.objects.all()
    permission_classes = [permissions.IsAuthenticated]
    serializer_class = serializers.CreditSerializer
    def create(self, request, *args, **kwargs):
        serializer = (self.get_serializer(data=request.data))
        serializer.is_valid(raise_exception=True)
        print(serializer.data)
        user = Account.objects.get(pk=int(serializer.data['user']))
        response_data = count_credit(user, float(serializer.data['salary']), serializer.data['pledge'],
                            float(serializer.data['price']), float(serializer.data['paid']), bool(request.data["j_check"]))
        headers = self.get_success_headers(serializer.data)
        return response.Response(response_data, status=status.HTTP_201_CREATED, headers=headers)



class FindCredit(generics.ListAPIView):
    queryset = Credit.objects.all()
    serializer_class = serializers.CreditSerializer
    permissions_classes = [permissions.IsAdminUser]

    def get_queryset(self, *args, **kwargs):
        q = self.request.GET.get('q')
        f = self.request.GET.get('f')
        if q and f is not None:
            if f == 'iden_num' or f == 'username':
                result = Credit.objects.search(q, f)
                if result.exists():
                    return result
                return Credit.objects.none()
            return Credit.objects.none()


class FindTransfer(generics.ListAPIView):
    queryset = Transfer.objects.all()
    serializer_class = serializers.TransferSerializer
    permissions_classes = [permissions.IsAdminUser]

    def get_queryset(self, *args, **kwargs):
        q = self.request.GET.get('q')
        f = self.request.GET.get('f')
        if q and f is not None:
            if f == 'trans_num' or f == 'username':
                result = Transfer.objects.search(q, f)
                if result.exists():
                    return result
                return Transfer.objects.none()
            return Transfer.objects.none()




