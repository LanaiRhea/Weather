import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from datetime import datetime
import json
import os
import mysql.connector

# 设置中文字体
plt.rcParams['font.sans-serif'] = ['SimHei']  # 正常显示中文
plt.rcParams['axes.unicode_minus'] = False  # 正常显示负号

def load_data_from_db():
    """从数据库加载数据"""
    try:
        conn = mysql.connector.connect(
            host='localhost',
            user='root',
            password='root',  # 修改为你的密码
            database='weather',
            port=3306
        )
        
        query = """
        SELECT time, rainfall, min_temp AS minTemp, max_temp AS maxTemp, max_wind_speed AS maxWindSpeed
        FROM daily_weather
        WHERE YEAR(time) = 2024
        ORDER BY time
        """
        
        df = pd.read_sql(query, conn)
        conn.close()

        df['time'] = pd.to_datetime(df['time'])
        df['month'] = df['time'].dt.month
        df['season'] = df['time'].dt.quarter

        # 转换为数值型，避免绘图出错
        df['rainfall'] = pd.to_numeric(df['rainfall'], errors='coerce')
        df['minTemp'] = pd.to_numeric(df['minTemp'], errors='coerce')
        df['maxTemp'] = pd.to_numeric(df['maxTemp'], errors='coerce')
        df['maxWindSpeed'] = pd.to_numeric(df['maxWindSpeed'], errors='coerce')
        
        return df

    except Exception as e:
        print(f"数据库连接或读取失败: {str(e)}")
        raise

def generate_analysis(df, output_dir):
    """生成图表与统计JSON"""
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)

    # 温度变化趋势
    plt.figure(figsize=(15, 8))
    plt.plot(df['time'], df['maxTemp'], label='最高温度', color='#F56C6C', linewidth=2)
    plt.plot(df['time'], df['minTemp'], label='最低温度', color='#409EFF', linewidth=2)
    plt.fill_between(df['time'], df['minTemp'], df['maxTemp'], alpha=0.2, color='#909399')
    plt.title('2024年温度变化趋势', fontsize=14)
    plt.xlabel('日期')
    plt.ylabel('温度 (°C)')
    plt.legend()
    plt.grid(True, alpha=0.3)
    plt.tight_layout()
    plt.savefig(f'{output_dir}/temperature_trend.png', dpi=300)
    plt.close()

    # 月度降水量
    plt.figure(figsize=(15, 8))
    monthly_rainfall = df.groupby('month')['rainfall'].sum()
    ax = monthly_rainfall.plot(kind='bar', color='#409EFF', alpha=0.7)
    plt.title('2024年月度降水量分布', fontsize=14)
    plt.xlabel('月份')
    plt.ylabel('降水量 (mm)')
    for i, v in enumerate(monthly_rainfall):
        ax.text(i, v + 1, f'{v:.1f}', ha='center', fontsize=10)
    plt.grid(True, alpha=0.3, axis='y')
    plt.tight_layout()
    plt.savefig(f'{output_dir}/monthly_rainfall.png', dpi=300)
    plt.close()

    # 统计数据
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
            'monthly_distribution': df.groupby('month')['rainfall'].sum().round(1).to_dict()
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

    with open(f'{output_dir}/stats.json', 'w', encoding='utf-8') as f:
        json.dump(stats, f, ensure_ascii=False, indent=2)

def main():
    try:
        output_dir = 'D:/Code/Weather/weather-web/public/analysis_results'
        print("正在从数据库加载数据...")
        df = load_data_from_db()
        print("正在生成分析报告和图表...")
        generate_analysis(df, output_dir)
        print(f"分析完成，结果保存于：{output_dir}")
    except Exception as e:
        print(f"程序出错: {str(e)}")

if __name__ == '__main__':
    main()
