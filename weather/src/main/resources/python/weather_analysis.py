import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from datetime import datetime
import json
import os

# 设置中文字体
plt.rcParams['font.sans-serif'] = ['SimHei']  # 用来正常显示中文标签
plt.rcParams['axes.unicode_minus'] = False  # 用来正常显示负号

def load_data(file_path):
    """从数据库导出的CSV文件加载数据"""
    df = pd.read_csv(file_path)
    df['time'] = pd.to_datetime(df['time'])
    df['month'] = df['time'].dt.month
    df['season'] = df['time'].dt.quarter
    return df

def generate_analysis(df, output_dir):
    """生成完整的数据分析报告和图表"""
    # 创建输出目录
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)
        
    # 1. 温度分析
    plt.figure(figsize=(15, 8))
    plt.plot(df['time'], df['maxTemp'], label='最高温度', color='red')
    plt.plot(df['time'], df['minTemp'], label='最低温度', color='blue')
    plt.fill_between(df['time'], df['minTemp'], df['maxTemp'], alpha=0.2)
    plt.title('2024年温度变化趋势')
    plt.xlabel('日期')
    plt.ylabel('温度 (°C)')
    plt.legend()
    plt.grid(True)
    plt.savefig(f'{output_dir}/temperature_trend.png')
    plt.close()

    # 2. 降水量分析
    plt.figure(figsize=(15, 8))
    monthly_rainfall = df.groupby('month')['rainfall'].sum()
    monthly_rainfall.plot(kind='bar')
    plt.title('2024年月度降水量分布')
    plt.xlabel('月份')
    plt.ylabel('降水量 (mm)')
    plt.grid(True)
    plt.savefig(f'{output_dir}/monthly_rainfall.png')
    plt.close()

    # 3. 温度与降水关系散点图
    plt.figure(figsize=(10, 8))
    plt.scatter(df['maxTemp'], df['rainfall'], alpha=0.5)
    plt.title('温度与降水量关系')
    plt.xlabel('最高温度 (°C)')
    plt.ylabel('降水量 (mm)')
    plt.grid(True)
    plt.savefig(f'{output_dir}/temp_rain_correlation.png')
    plt.close()

    # 4. 风速分布
    plt.figure(figsize=(12, 6))
    sns.histplot(data=df, x='maxWindSpeed', bins=30)
    plt.title('2024年风速分布')
    plt.xlabel('最大风速 (m/s)')
    plt.ylabel('频次')
    plt.savefig(f'{output_dir}/wind_distribution.png')
    plt.close()

    # 5. 生成统计报告
    stats = {
        'temperature': {
            'max_temp': {
                'value': float(df['maxTemp'].max()),
                'date': df.loc[df['maxTemp'].idxmax(), 'time'].strftime('%Y-%m-%d'),
                'avg': float(df['maxTemp'].mean()),
                'std': float(df['maxTemp'].std())
            },
            'min_temp': {
                'value': float(df['minTemp'].min()),
                'date': df.loc[df['minTemp'].idxmin(), 'time'].strftime('%Y-%m-%d'),
                'avg': float(df['minTemp'].mean()),
                'std': float(df['minTemp'].std())
            }
        },
        'rainfall': {
            'total': float(df['rainfall'].sum()),
            'max': {
                'value': float(df['rainfall'].max()),
                'date': df.loc[df['rainfall'].idxmax(), 'time'].strftime('%Y-%m-%d')
            },
            'rainy_days': int(df['rainfall'][df['rainfall'] > 0].count()),
            'monthly_distribution': df.groupby('month')['rainfall'].sum().to_dict()
        },
        'wind': {
            'max_speed': {
                'value': float(df['maxWindSpeed'].max()),
                'date': df.loc[df['maxWindSpeed'].idxmax(), 'time'].strftime('%Y-%m-%d')
            },
            'avg_speed': float(df['maxWindSpeed'].mean())
        },
        'extreme_weather': {
            'high_temp_days': int(df[df['maxTemp'] > 30].shape[0]),
            'low_temp_days': int(df[df['minTemp'] < 0].shape[0]),
            'heavy_rain_days': int(df[df['rainfall'] > 25].shape[0]),
            'strong_wind_days': int(df[df['maxWindSpeed'] > 10.7].shape[0])
        }
    }

    # 保存统计报告
    with open(f'{output_dir}/stats.json', 'w', encoding='utf-8') as f:
        json.dump(stats, f, ensure_ascii=False, indent=2)

    # 生成Markdown格式的分析报告
    report = f"""# 2024年舟山市气象数据分析报告

## 1. 温度分析
- 最高温度: {stats['temperature']['max_temp']['value']:.1f}°C ({stats['temperature']['max_temp']['date']})
- 最低温度: {stats['temperature']['min_temp']['value']:.1f}°C ({stats['temperature']['min_temp']['date']})
- 平均最高温度: {stats['temperature']['max_temp']['avg']:.1f}°C
- 平均最低温度: {stats['temperature']['min_temp']['avg']:.1f}°C

## 2. 降水分析
- 年总降水量: {stats['rainfall']['total']:.1f}mm
- 最大日降水量: {stats['rainfall']['max']['value']:.1f}mm ({stats['rainfall']['max']['date']})
- 降水天数: {stats['rainfall']['rainy_days']}天

## 3. 风速分析
- 最大风速: {stats['wind']['max_speed']['value']:.1f}m/s ({stats['wind']['max_speed']['date']})
- 平均风速: {stats['wind']['avg_speed']:.1f}m/s

## 4. 极端天气统计
- 高温天数(>30°C): {stats['extreme_weather']['high_temp_days']}天
- 低温天数(<0°C): {stats['extreme_weather']['low_temp_days']}天
- 暴雨天数(>25mm): {stats['extreme_weather']['heavy_rain_days']}天
- 大风天数(>10.7m/s): {stats['extreme_weather']['strong_wind_days']}天

## 5. 月度降水分布
"""
    # 添加月度降水量数据
    for month, rainfall in stats['rainfall']['monthly_distribution'].items():
        report += f"- {month}月: {rainfall:.1f}mm\n"

    # 保存报告
    with open(f'{output_dir}/report.md', 'w', encoding='utf-8') as f:
        f.write(report)

def main():
    # 设置输入输出路径
    data_file = 'weather_data_2024.csv'  # 从数据库导出的CSV文件
    output_dir = 'analysis_results'
    
    # 加载数据
    df = load_data(data_file)
    
    # 生成分析报告和图表
    generate_analysis(df, output_dir)

if __name__ == '__main__':
    main() 