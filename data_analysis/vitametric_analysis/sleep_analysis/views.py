from django.http import JsonResponse
import json
from .service import SleepAnalysisService


def predict_sleep_duration(request):
    try:
        data = json.loads(request.body)
        date_str = data.get('date')

        if not date_str:
            return JsonResponse({'error': 'Date not provided in request'}, status=400)

        service = SleepAnalysisService()
        prediction = service.predict_sleep_duration(date_str)

        if prediction is None:
            return JsonResponse({'error': 'No sleep records found'}, status=404)

        return JsonResponse({'predicted sleep': prediction})

    except ValueError:
        return JsonResponse({'error': 'Invalid date format. Expected format: YYYY-MM-DD'}, status=400)

    except Exception as e:
        return JsonResponse({'error': str(e)}, status=500)

