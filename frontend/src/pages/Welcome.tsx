import React, { useState, useEffect } from 'react';
import { Card, Layout, Row, Col, Statistic, Calendar, Typography, Spin } from 'antd';
import { motion } from 'framer-motion';
import moment from 'moment';

const { Content } = Layout;
const { Title, Paragraph, Text } = Typography;

const HomePage = () => {
  const today = moment();
  const currentDate = today.format('YYYY-MM-DD');
  const currentDay = today.format('dddd');
  const [currentTime, setCurrentTime] = useState(moment().format('HH:mm:ss'));

  // 存储诗句数据
  const [quote, setQuote] = useState('');
  const [quoteFrom, setQuoteFrom] = useState('');
  const [quoteAuthor, setQuoteAuthor] = useState('');
  const [loadingQuote, setLoadingQuote] = useState(true);

  // 获取诗句 API
  const fetchQuote = async () => {
    try {
      const response = await fetch('https://v1.hitokoto.cn/?c=i&encode=json');
      const data = await response.json();
      setQuote(data.hitokoto);       // 诗句
      setQuoteFrom(data.from);        // 诗句出处
      setQuoteAuthor(data.from_who);  // 诗句作者
    } catch (error) {
      // 失败时使用备用诗句
      setQuote('学而不思则罔，思而不学则殆');
      setQuoteFrom('论语');
      setQuoteAuthor('孔子');
    } finally {
      setLoadingQuote(false);
    }
  };


  // 组件加载时获取诗句
  useEffect(() => {
    fetchQuote();
    const interval = setInterval(() => {
      setCurrentTime(moment().format('HH:mm:ss'));
    }, 1000);

    return () => clearInterval(interval);
  }, []);


  return (
    <Content style={{ padding: '24px', background: '#f0f2f5', minHeight: '100vh' }}>
      {/* 顶部标题与日期信息 */}
      <motion.div initial={{ opacity: 0, y: -20 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.5 }}>
        <Row justify="space-between" align="middle" style={{ marginBottom: 24 }}>
          <Col>
            <Title level={2}>课业星⭐</Title>
          </Col>
          <Col>
            <Statistic title="当前时间" value={`${currentDate} ${currentTime}`} />
            <Statistic title="星期" value={currentDay} style={{ marginTop: 8 }} />
          </Col>
        </Row>
      </motion.div>


      {/* 名言提醒 */}
      <motion.div initial={{ opacity: 0, scale: 0.95 }} animate={{ opacity: 1, scale: 1 }} transition={{ duration: 0.5, delay: 0.4 }} style={{ marginTop: 24 }}>
        <Card title="每日诗句" bordered={true} hoverable style={{ position: 'relative' }}>
          {loadingQuote ? (
            <Spin />
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', padding: '16px' }}>
              {/* 诗句 */}
              <Paragraph style={{ fontSize: '18px', fontWeight: 'bold', textAlign: 'center', marginBottom: '12px' }}>
                “ {quote} ”
              </Paragraph>
              {/* 出处 + 作者（右下角） */}
              <Text type="secondary" style={{ position: 'absolute', right: '16px', bottom: '10px' }}>
                —— {quoteAuthor || '佚名'} · 《{quoteFrom || '未知'}》
              </Text>
            </div>
          )}
        </Card>
      </motion.div>

      {/* 日历组件 */}
      <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} transition={{ duration: 0.5, delay: 0.6 }} style={{ marginTop: 24 }}>
        <Card title="日历" bordered={false}>
          <Calendar fullscreen={false} />
        </Card>
      </motion.div>
    </Content>
  );
};

export default HomePage;
