# Generated by Django 5.0.6 on 2024-06-06 17:50

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('account', '0008_alter_account_identify_number'),
    ]

    operations = [
        migrations.AlterField(
            model_name='account',
            name='identify_number',
            field=models.CharField(default='4538944475', max_length=10),
        ),
    ]
