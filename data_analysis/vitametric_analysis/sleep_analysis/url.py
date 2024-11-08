from django.urls import path

from . import views

urlpatterns = [
    path('predict', views.predict_sleep_duration, name='predict_sleep_duration')
]
