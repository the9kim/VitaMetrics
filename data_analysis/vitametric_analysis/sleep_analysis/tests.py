import json
from datetime import datetime
from unittest.mock import patch, MagicMock

import pandas as pd
from django.test import TestCase
from django.urls import reverse
from rest_framework.test import APIClient

from vitametric_analysis.sleep_analysis.service import SleepAnalysisService


class SleepAnalysisServiceTest(TestCase):
    def setUp(self):
        self.service = SleepAnalysisService()

    @patch('vitametric_analysis.sleep_analysis.service.joblib.load')
    def test_load_model(self, mock_joblib_load):
        # Mock loading the model
        mock_joblib_load.return_value = MagicMock()
        model_path = '~/VitaMetrics/data_analysis/vitametric_analysis/sleep_prediction_model.pkl'
        model = self.service.load_model(model_path)
        self.assertIsNotNone(model)
        mock_joblib_load.assert_called_once()

    @patch('vitametric_analysis.sleep_analysis.service.SleepAnalysisService._prepare_input')
    @patch('vitametric_analysis.sleep_analysis.service.joblib.load')
    def test_predict_sleep_duration(self, mock_joblib_load, mock_prepare_input):
        # Mock dependencies
        mock_model = MagicMock()
        mock_model.predict.return_value = 470.0  # 420 minutes = 7 hours
        mock_joblib_load.return_value = mock_model
        mock_prepare_input.return_value = MagicMock()

        service = SleepAnalysisService()
        prediction = service.predict_sleep_duration('2024-11-17')

        self.assertEqual(prediction, {"hours": 7, "minutes": 50})
        mock_prepare_input.assert_called_once()
        mock_model.predict.assert_called_once()

    def test_invalid_date_format(self):
        with self.assertRaises(ValueError) as context:
            self.service.predict_sleep_duration('invalid-date')
        self.assertEqual(str(context.exception), "Invalid Date format. Use 'YYYY-MM-DD'.")

    def test_add_time_features(self):
        df = self.service._add_time_features(pd.DataFrame({
            'creationDate': [datetime(2024, 11, 17)]
        }))
        self.assertEqual(df.loc[0, 'dayofweek'], 6)  # Sunday
        self.assertEqual(df.loc[0, 'year'], 2024)
        self.assertEqual(df.loc[0, 'quater'], 4)

    def test_format_prediction(self):
        result = self.service._format_prediction(395.5)  # 6 hours 35 minutes
        self.assertEqual(result, {"hours": 6, "minutes": 35})


class PredictSleepDurationAPITest(TestCase):
    def setUp(self):
        self.client = APIClient()
        self.url = reverse('predict_sleep_duration')

    @patch('vitametric_analysis.sleep_analysis.views.SleepAnalysisService')
    def test_predict_sleep_duration_success(self, mock_service_class):
        mock_service_instance = mock_service_class.return_value
        mock_service_instance.predict_sleep_duration.return_value = {'hours': 7, 'minutes': 50}

        response = self.client.post(
            self.url,
            data=json.dumps({'date': '2024-11-17'}),
            content_type='application/json'
        )

        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), {'hours': 7, 'minutes': 50})

    def test_predict_sleep_duration_missing_date(self):
        response = self.client.post(self.url, data=json.dumps({}), content_type='application/json')
        self.assertEqual(response.status_code, 400)
        self.assertEqual(response.json(), {'error': 'Date not provided in request'})

    def test_predict_sleep_duration_invalid_date(self):
        response = self.client.post(
            self.url,
            data=json.dumps({'date': 'invalid-date'}),
            content_type='application/json'
        )
        self.assertEqual(response.status_code, 400)
        self.assertEqual(response.json(), {'error': 'Invalid date format. Expected format: YYYY-MM-DD'})

    # @patch('vitametric_analysis.sleep_analysis.views.SleepAnalysisService')
    # def test_predict_sleep_duration_exception(self, mock_service_class):
    #     mock_service_instance = mock_service_class.return_value
    #     mock_service_instance.predict_sleep_duration.side_effect = Exception("Test exception")
    #
    #     response = self.client.post(
    #         self.url,
    #         data=json.dumps({'date': '2024-11-17'}),
    #         content_type='application/json'
    #     )
    #     self.assertEqual(response.status_code, 500)
    #     self.assertEqual(response.json(), {'error': 'Test exception'})
