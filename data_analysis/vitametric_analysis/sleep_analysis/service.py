import os
from datetime import datetime

import joblib
import pandas as pd


class SleepAnalysisService:

    def __init__(self):
        model_path = os.getenv('MODEL_LOCATION', os.getcwd())
        model_path = os.path.join(model_path, 'sleep_prediction_model.pkl')
        self.model = self.load_model(model_path)

    @staticmethod
    def load_model(model_path):
        try:
            return joblib.load(model_path)
        except FileNotFoundError:
            raise Exception(f"Model file not found at path: {model_path}")

    def predict_sleep_duration(self, date: str) -> dict[str, int]:
        try:
            date_obj = datetime.strptime(date, '%Y-%m-%d')  # Ensure 'YYYY-MM-DD' format
        except ValueError:
            raise ValueError("Invalid Date format. Use 'YYYY-MM-DD'.")

        input_data = self._prepare_input(date_obj)
        predicted_time = self.model.predict(input_data)
        return self._format_prediction(predicted_time)

    def _prepare_input(self, date: datetime) -> pd.DataFrame:

        sleep_data = pd.DataFrame({'creationDate': [date]})
        sleep_data['creationDate'] = pd.to_datetime(sleep_data['creationDate'])

        sleep_data = self._add_time_features(sleep_data)
        sleep_data.set_index('creationDate', inplace=True)

        return sleep_data

    @staticmethod
    def _add_time_features(sleep_data: pd.DataFrame) -> pd.DataFrame:
        sleep_data['dayofweek'] = sleep_data.creationDate.dt.dayofweek
        sleep_data['quater'] = sleep_data.creationDate.dt.quarter
        sleep_data['year'] = sleep_data.creationDate.dt.year
        sleep_data['dayofyear'] = sleep_data.creationDate.dt.dayofyear
        sleep_data['dayofmonth'] = sleep_data.creationDate.dt.day
        sleep_data['weekofyear'] = sleep_data.creationDate.dt.isocalendar().week

        return sleep_data

    @staticmethod
    def _format_prediction(predicted_minutes: float) -> dict[str, int]:
        hours = int(predicted_minutes // 60)
        minutes = int(predicted_minutes % 60)
        return {"hours": hours, "minutes": minutes}


if __name__ == '__main__':
    service = SleepAnalysisService()
    prediction = service.predict_sleep_duration('2024-11-17')
    print(prediction)
